package `is`.vr.mpv

import kotlin.math.*

data class IMUData(
    val gx: Float,
    val gy: Float,
    val gz: Float,
    val ax: Float,
    val ay: Float,
    val az: Float
)

data class HeadOrientation(
    val pitch: Float,
    val yaw: Float,
    val roll: Float
)

class HeadTracker {

    private val calibrationSamples = mutableListOf<FloatArray>()
    private var calibrationCount = 0
    private val calibrationTarget = 1500
    private var isCalibrated = false

    private var gyroBiasX = 0f
    private var gyroBiasY = 0f
    private var gyroBiasZ = 0f

    var pitch = 0f; private set
    var yaw   = 0f; private set
    var roll  = 0f; private set

    private var zeroPitch = 0f
    private var zeroYaw   = 0f
    private var zeroRoll  = 0f

    // Timing
    private var lastTimeNs = 0L

    private val alpha = 0.98f

    // Scaling for VR output range
    private val pitchScale = 1.5f
    private val yawScale   = -1.5f
    private val rollScale  = 0.5f

    // -------------------------------------------------------------------------
    // Calibration
    // -------------------------------------------------------------------------

    /**
     * Feed IMU samples while the device is stationary.
     * Returns true once calibration is complete.
     */
    fun calibrate(imuData: IMUData): Boolean {
        if (isCalibrated) return true

        calibrationSamples.add(floatArrayOf(imuData.gx, imuData.gy, imuData.gz))
        calibrationCount++

        if (calibrationCount >= calibrationTarget) {
            gyroBiasX = calibrationSamples.map { it[0] }.average().toFloat()
            gyroBiasY = calibrationSamples.map { it[1] }.average().toFloat()
            gyroBiasZ = calibrationSamples.map { it[2] }.average().toFloat()

            pitch = 0f; yaw = 0f; roll = 0f
            lastTimeNs = 0L
            isCalibrated = true

            val ax = imuData.ax; val ay = imuData.ay; val az = imuData.az
            val accMag = sqrt(ax * ax + ay * ay + az * az)
            if (accMag > 0.01f) {
                pitch = atan2(-ax, sqrt(ay * ay + az * az)) * (180f / PI.toFloat())
                roll  = atan2(ay, az) * (180f / PI.toFloat())
                yaw   = 0f
            }

            zeroView()
        }

        return isCalibrated
    }

    fun resetCalibration() {
        calibrationSamples.clear()
        calibrationCount = 0
        isCalibrated = false
        gyroBiasX = 0f; gyroBiasY = 0f; gyroBiasZ = 0f
    }

    val calibrationProgress: Float
        get() = (calibrationCount.toFloat() / calibrationTarget) * 100f

    // -------------------------------------------------------------------------
    // Zero / reference
    // -------------------------------------------------------------------------

    /** Snap current orientation to be the new "forward" reference. */
    fun zeroView() {
        zeroPitch = pitch
        zeroYaw   = yaw
        zeroRoll  = roll
    }

    /** Orientation relative to the zero reference, scaled for VR use. */
    fun getRelativeOrientation(): HeadOrientation = HeadOrientation(
        pitch = wrapAngle((pitch - zeroPitch) * pitchScale),
        yaw   = wrapAngle((yaw   - zeroYaw)   * yawScale),
        roll  = wrapAngle((roll  - zeroRoll)   * rollScale)
    )

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------
    // Add these variables to your class properties
    // 1. Add a persistent internal accumulator
    // 1. Ensure these are initialized to 0f

    // Accumulated deltas (persist between frames)
// Accumulated deltas
// 1. Define your raw internal angles
    private var rawPitch = 0f
    private var rawYaw = 0f
    private var rawRoll = 0f

    // 2. Instantiate the smoothers for your output
    val pitchSmoother = SmartSmoother()
    val yawSmoother = SmartSmoother()
    val rollSmoother = SmartSmoother()

    val radToDeg = 180f / Math.PI.toFloat()

    fun update(imuData: IMUData, timestampNs: Long) {
        if (!isCalibrated) return

        if (lastTimeNs == 0L) {
            lastTimeNs = timestampNs
            return
        }

        val dt = (timestampNs - lastTimeNs) / 1_000_000_000f
        lastTimeNs = timestampNs


        // Remove bias AND convert to degrees per second
        val gx = (imuData.gx - gyroBiasX) * radToDeg
        val gy = (imuData.gy - gyroBiasY) * radToDeg
        val gz = (imuData.gz - gyroBiasZ) * radToDeg

        // Now everything is in degrees!
        val pitchGyro = rawPitch + gx * dt
        val yawGyro   = rawYaw   + gy * dt
        val rollGyro  = rawRoll  + gz * dt

        val ax = imuData.ax; val ay = imuData.ay; val az = imuData.az
        val accMag = sqrt(ax * ax + ay * ay + az * az)

        if (accMag > 0.01f) {
            val pitchAccel = atan2(-ax, sqrt(ay * ay + az * az)) * (180f / PI.toFloat())
            val rollAccel  = atan2(ay, az) * (180f / PI.toFloat())

            // Update raw internal states
            rawPitch = wrapAngle(alpha * pitchGyro + (1f - alpha) * pitchAccel)
            rawYaw   = wrapAngle(yawGyro)
            rawRoll  = wrapAngle(alpha * rollGyro  + (1f - alpha) * rollAccel)
        } else {
            rawPitch = wrapAngle(pitchGyro)
            rawYaw   = wrapAngle(yawGyro)
            rawRoll  = wrapAngle(rollGyro)
        }

        // --- SMOOTHING LOGIC ---
        // Feed the raw continuous angles into the smoothers to get the deadband effect
        pitch = pitchSmoother.update(rawPitch, dt)
        yaw   = yawSmoother.update(rawYaw, dt)
        roll  = rollSmoother.update(rawRoll, dt)
    }

    class SmartSmoother(
        var currentAngle: Float = 0f,
        private val deadzone: Float = 1.5f,   // Total silence below this
        private val maxSpeed: Float = 8.0f    // Catch-up speed for large moves
    ) {
        fun update(targetRawAngle: Float, dt: Float): Float {
            val diff = wrapAngle(targetRawAngle - currentAngle)
            val absDiff = kotlin.math.abs(diff)

            // 1. If inside deadzone, stay perfectly still
            if (absDiff < deadzone) {
                return currentAngle
            }

            // 2. Calculate "Pressure"
            // This is 0.0 at the edge of the deadzone, and scales up to 1.0
            // as the difference increases.
            val pressure = ((absDiff - deadzone) / 8.0f).coerceIn(0f, 1f)

            // 3. Dynamic Smoothing
            // We use a non-linear speed: very slow when pressure is low,
            // fast when pressure is high.
            val dynamicSpeed = maxSpeed * (pressure * pressure)

            val step = diff * (dynamicSpeed * dt).coerceAtMost(1f)
            currentAngle = wrapAngle(currentAngle + step)

            return currentAngle
        }

        private fun wrapAngle(angle: Float): Float {
            var a = angle
            while (a >  180f) a -= 360f
            while (a < -180f) a += 360f
            return a
        }
    }

    class SmoothedAngle(
        var currentAngle: Float = 0f,
        private val startThreshold: Float = 0.5f, // Start moving if diff > 2 deg
        private val stopThreshold: Float = 0.1f,  // Stop moving when diff < 0.1 deg
        private val smoothSpeed: Float = 4.0f     // Higher = faster catch-up
    ) {
        private var isMoving = false

        fun update(targetRawAngle: Float, dt: Float): Float {
            // Calculate the shortest angular distance between current and target
            val diff = wrapAngle(targetRawAngle - currentAngle)
            val absDiff = abs(diff)

            // Hysteresis logic: start or stop moving based on thresholds
            if (!isMoving && absDiff > startThreshold) {
                isMoving = true
            } else if (isMoving && absDiff < stopThreshold) {
                isMoving = false
                currentAngle = targetRawAngle // Snap the final tiny fraction to prevent micro-wobbles
                return currentAngle
            }

            // If we are in the moving state, smoothly interpolate towards the target
            if (isMoving) {
                // LERP towards target. coerceAtMost(1f) prevents overshoot on large dt spikes
                val step = diff * (smoothSpeed * dt).coerceAtMost(1f)
                currentAngle = wrapAngle(currentAngle + step)
            }

            return currentAngle
        }

        private fun wrapAngle(angle: Float): Float {
            var a = angle
            while (a >  180f) a -= 360f
            while (a < -180f) a += 360f
            return a
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private fun wrapAngle(angle: Float): Float {
        var a = angle
        while (a >  180f) a -= 360f
        while (a < -180f) a += 360f
        return a
    }
}