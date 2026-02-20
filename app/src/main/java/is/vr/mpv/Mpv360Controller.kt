package `is`.vr.mpv

import android.util.Log
import `is`.xyz.mpv.MPVLib
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.abs

class Mpv360Controller() {

    data class Config(
        var yaw: Double = 0.0,
        var pitch: Double = 0.0,
        var roll: Double = 0.0,
        var fov: Double = Math.toRadians(100.0),
        var inputProjection: Int = 2,
        var eye: Int = 2,
        var fisheyeFov: Double = Math.toRadians(180.0),
        var mouseSensitivity: Double = Math.toRadians(0.13),
        var step: Double = Math.toRadians(2.5),
        var fisheyeFovStep: Double = Math.toRadians(5.0),
        var enabled: Boolean = false,
        var showValues: Boolean = true
    )

    val config = Config()

    private var initialPos = Config()
    private var lastMouseX: Float = 0f
    private var lastMouseY: Float = 0f
    var mouseLookActive: Boolean = false
        private set

    companion object {
        private const val TAG = "Mpv360Controller"

        val PROJECTION_NAMES = mapOf(
            0 to "Equirectangular",
            1 to "Dual Fisheye",
            2 to "Dual Half-Equirectangular",
            3 to "Half-Equirectangular",
            4 to "Dual Equirectangular (Vert)",
            5 to "Cylindrical",
            6 to "Equi-Angular Cubemap",
            7 to "Dual Equi-Angular Cubemap"
        )

        val EYE_NAMES = mapOf(
            0 to "Left",
            1 to "Right",
            2 to "Half SBS",
            3 to "Full SBS"
        )

        private const val EPS = 1e-6
        private const val MAX_PROJECTION = 7
        private const val MAX_EYE = 3
    }

    // --- Init ---

    init {
        saveInitialPos()
    }

    private fun saveInitialPos() {
        initialPos = config.copy()
    }

    // --- Helpers ---

    private fun clamp(value: Double, min: Double, max: Double) = value.coerceIn(min, max)

    private fun normalize(angle: Double): Double {
        var a = angle
        while (a > Math.PI) a -= 2 * Math.PI
        while (a < -Math.PI) a += 2 * Math.PI
        return a
    }

    val isDualEye get() = config.inputProjection in setOf(1, 2, 4, 7)
    val isFisheye get() = config.inputProjection == 1

    fun getProjectionName() = PROJECTION_NAMES[config.inputProjection] ?: "Unknown"
    fun getEyeName() = EYE_NAMES[config.eye] ?: "Unknown"

    // --- Core: push params to mpv ---

    private val isBusy = AtomicBoolean(false)

    fun updateParams() {
        // If mpv is still processing last command, just drop this frame
        if (!isBusy.compareAndSet(false, true)) return


        // Clamp/normalize all values
        config.roll = clamp(normalize(config.roll), -Math.PI + EPS, Math.PI - EPS)
        config.pitch = clamp(config.pitch, -Math.PI / 2, Math.PI / 2)
        config.yaw = clamp(normalize(config.yaw), -Math.PI, Math.PI)
        config.fov = clamp(config.fov, EPS, Math.PI - EPS)
        config.inputProjection =
            clamp(config.inputProjection.toDouble(), 0.0, MAX_PROJECTION.toDouble()).toInt()
        config.eye = clamp(config.eye.toDouble(), 0.0, MAX_EYE.toDouble()).toInt()
        config.fisheyeFov = clamp(config.fisheyeFov, EPS, 2 * Math.PI)

        if (!config.enabled) return

        Thread {
            try {
                val params = buildString {
                    append("mpv360/fov=${config.fov},")
                    append("mpv360/yaw=${config.yaw},")
                    append("mpv360/pitch=${config.pitch},")
                    append("mpv360/roll=${config.roll},")
                    append("mpv360/input_projection=${config.inputProjection},")
                    append("mpv360/fisheye_fov=${config.fisheyeFov},")
                    append("mpv360/eye=${config.eye},")
                }

                MPVLib.command(arrayOf("no-osd", "change-list", "glsl-shader-opts", "add", params))
                //Log.d(TAG, "Updated params: $params")

                if (config.showValues) {
                    MPVLib.command(arrayOf("show-text", getStatusString()))
                }
            } finally {
                isBusy.set(false)
            }
        }.start()
    }

    // --- Commands (mirrors Lua commands table) ---

    fun toggle() {
        if (config.enabled) disable() else enable()
    }

    fun enable() {
        config.enabled = true
        updateParams()
        // Add the shader - adjust path to match your asset/shader location
        MPVLib.command(arrayOf("no-osd", "change-list", "glsl-shaders", "append", "~~/mpv360.glsl"))
        MPVLib.command(arrayOf("no-osd", "set", "keepaspect", "no"))
        Log.d(TAG, "360° mode enabled - ${getProjectionName()}")
    }

    fun disable() {
        stopMouseLook()
        MPVLib.command(arrayOf("no-osd", "change-list", "glsl-shaders", "remove", "~~/mpv360.glsl"))
        MPVLib.command(arrayOf("no-osd", "set", "keepaspect", "yes"))
        config.enabled = false
        Log.d(TAG, "360° mode disabled")
    }

    fun lookUp() {
        config.pitch += config.step; updateParams()
    }

    fun lookDown() {
        config.pitch -= config.step; updateParams()
    }

    fun lookLeft() {
        config.yaw -= config.step; updateParams()
    }

    fun lookRight() {
        config.yaw += config.step; updateParams()
    }

    fun rollLeft() {
        config.roll -= config.step; updateParams()
    }

    fun rollRight() {
        config.roll += config.step; updateParams()
    }

    fun fovIncrease() {
        config.fov += config.step; updateParams()
    }

    fun fovDecrease() {
        config.fov -= config.step; updateParams()
    }

    fun fisheyeFovIncrease() {
        config.fisheyeFov += config.fisheyeFovStep; updateParams()
    }

    fun fisheyeFovDecrease() {
        config.fisheyeFov -= config.fisheyeFovStep; updateParams()
    }

    fun resetView() {
        config.yaw = initialPos.yaw
        config.pitch = initialPos.pitch
        config.roll = initialPos.roll
        config.fov = initialPos.fov
        updateParams()
    }

    fun cycleProjection() {
        config.inputProjection = (config.inputProjection + 1) % (MAX_PROJECTION + 1)
        updateParams()
    }

    fun switchEye() {
        if (isDualEye) {
            config.eye = (config.eye + 1) % (MAX_EYE + 1)
            updateParams()
        } else {
            Log.w(TAG, "Eye selection only available for dual eye formats.")
        }
    }

    // --- Mouse look ---

    fun startMouseLook() {
        if (!config.enabled || mouseLookActive) return
        mouseLookActive = true
        Log.d(TAG, "Mouse look enabled")
    }

    fun stopMouseLook() {
        if (!mouseLookActive) return
        mouseLookActive = false
        Log.d(TAG, "Mouse look disabled")
    }

    fun toggleMouseLook() {
        if (mouseLookActive) stopMouseLook() else startMouseLook()
    }

    fun toggleShowValues() {
        config.showValues = !config.showValues
    }

    /**
     * Call this from your touch/sensor handler when mouse look is active.
     * dx/dy are pixel deltas.
     */
    fun onMouseMove(dx: Float, dy: Float) {
        config.yaw += dx * config.mouseSensitivity;
        config.pitch -= dy * config.mouseSensitivity;
        updateParams()
    }

    fun onMouseStart(x: Float, y: Float) {
        lastMouseX = x
        lastMouseY = y
    }

    /**
     * Convenience: pass raw absolute positions and let the controller compute deltas.
     */
    fun onMousePosition(x: Float, y: Float) {
        if (!mouseLookActive) {
            lastMouseX = x
            lastMouseY = y
            return
        }

        val dx = x - lastMouseX
        val dy = y - lastMouseY
        lastMouseX = x
        lastMouseY = y

        onMouseMove(dx, dy)
    }

    /**
     * XReal / IMU sensor input: absolute yaw/pitch/roll in degrees.
     */
    private var lastSentYaw = 0f
    private var lastSentPitch = 0f
    private var lastSentRoll = 0f
    private val MIN_DELTA_DEG = 0.05f  // tune this, smaller = more responsive but more updates

    fun onXrealInput(yawDeg: Float, pitchDeg: Float, rollDeg: Float) {
        if (abs(yawDeg - lastSentYaw) < MIN_DELTA_DEG &&
            abs(pitchDeg - lastSentPitch) < MIN_DELTA_DEG &&
            abs(rollDeg - lastSentRoll) < MIN_DELTA_DEG) return

        lastSentYaw = yawDeg
        lastSentPitch = pitchDeg
        lastSentRoll = rollDeg

        config.yaw = Math.toRadians(yawDeg.toDouble())
        config.pitch = Math.toRadians(pitchDeg.toDouble())
        config.roll = Math.toRadians(rollDeg.toDouble())

        updateParams()
    }

    fun showCalibration(calibration: Float) {

        if (!isBusy.compareAndSet(false, true)) return
        Thread {
            try {
                if (config.showValues) {
                    MPVLib.command(arrayOf("show-text", "Calibration: ${"%.2f".format(calibration)}"))
                }
            } finally {
                isBusy.set(false)
            }
        }.start()

    }

    fun getStatusString(): String {
        val eyePart = if (isDualEye) " | Eye: ${getEyeName()}" else ""
        val fisheyePart =
            if (isFisheye) " | Fisheye FOV: ${"%.0f".format(Math.toDegrees(config.fisheyeFov))}°" else ""
        return "Proj: ${getProjectionName()}$fisheyePart$eyePart\n" +
                "Yaw: ${"%.1f".format(Math.toDegrees(config.yaw))}° | " +
                "Pitch: ${"%.1f".format(Math.toDegrees(config.pitch))}° | " +
                "Roll: ${"%.1f".format(Math.toDegrees(config.roll))}° | " +
                "FOV: ${"%.1f".format(Math.toDegrees(config.fov))}°"
    }

    fun videoStarted() {
        toggle()

//        val choreographer = Choreographer.getInstance()
//        val callback = object : Choreographer.FrameCallback {
//            override fun doFrame(frameTimeNanos: Long) {
//                choreographer.postFrameCallback(this)
//
//                config.yaw += 0.05
//                updateParams()
//            }
//        }
//
//        choreographer.postFrameCallback(callback)
    }
}