package com.xreal.glasses.api;

public class Control {
    private static volatile Control sInstance;
    private static ImuAccAndGyroCallback imuCallback;
    private static ImuMagCallback magCallback;
    private static INRControlCallback sCallback;

    public interface INRControlCallback {
        void onClientCountChanged(int i, int i2);

        void onGlassesAction(int i, int i2, int i3, long j);

        void onGlassesEvent(int i, int i2, int i3, int i4, int i5, byte[] bArr);
    }

    public static class NRGlassesControlParams {
        public byte[] glasses_control_params = new byte[16];
        public int glasses_control_params_type;
        public int length = 16;
    }

    public native boolean nativeEnterPowerSave();

    public native boolean nativeFWUPdate();

    public native boolean nativeForceEnterSleep(int i);

    public native int nativeGet2D3DMode();

    public native String nativeGet7211ICStatus(int i);

    public native int nativeGetActivatedState();

    public native int nativeGetAudioAlgorithm();

    public native int nativeGetBrightness();

    public native int nativeGetBrightnessLevelNumber();

    public native int nativeGetBrightnessSet();

    public native String nativeGetCompatibleGlassesID();

    public native String nativeGetCompatibleGlassesSN();

    public native String nativeGetCustomerID();

    public native String nativeGetCustomerSN();

    public native String nativeGetDPFwVersion();

    public native int nativeGetDisplayBrightnessLevel();

    public native int nativeGetDisplayBrightnessLevelCount();

    public native int nativeGetDisplayBypassPsensorFlag();

    public native int nativeGetDisplayColorCalibrationType();

    public native int nativeGetDisplayColorTemperatureLevel();

    public native int nativeGetDisplayColorTemperatureLevelCount();

    public native int nativeGetDisplayDefaultResolution();

    public native int nativeGetDisplayDefaultStartMode();

    public native int nativeGetDisplayDutyValue();

    public native int nativeGetDisplayLuminanceValue();

    public native NRGlassesControlParams nativeGetDisplayMapParams();

    public native int nativeGetDisplayPermitScreenEnable();

    public native int nativeGetDisplayScreenEnable();

    public native int nativeGetDisplayStereoMode();

    public native int nativeGetDpCurrentEdid();

    public native int nativeGetDpDataFilterMode();

    public native int nativeGetDpDataFilterModeCount();

    public native int nativeGetDpInputMode();

    public native int nativeGetDpWorkingState();

    public native String nativeGetDspVersion();

    public native int nativeGetDuty();

    public native int nativeGetEcLevel();

    public native int nativeGetEcLevelCount();

    public native int nativeGetEcValue(int i);

    public native int nativeGetElectrochromicLevel();

    public native int nativeGetElectrochromicTotalLevel();

    public native int nativeGetEnablePhysicalButtonSwitchDisplayModeFlag();

    public native int nativeGetFarThreshold();

    public native String nativeGetGlassesDspVersion();

    public native String nativeGetGlassesHWVersion();

    public native String nativeGetGlassesID();

    public native String nativeGetGlassesProductName();

    public native String nativeGetGlassesRunStatus(int i);

    public native String nativeGetGlassesSNCode(int i);

    public native String nativeGetGlassesSNValue(int i);

    public native String nativeGetGlassesSWVersion();

    public native int nativeGetGlassesStartupState();

    public native String nativeGetGlassesSystemVersion();

    public native int nativeGetGlassesUltraWideEnable();

    public native String nativeGetHWVersion();

    public native String nativeGetHostID();

    public native int nativeGetIfGlassesDisplayFine();

    public native long nativeGetImuInterruptCount();

    public native NRGlassesControlParams nativeGetLedRgbMode();

    public native int nativeGetLedWorkMode();

    public native int nativeGetLightIntensityState();

    public native int nativeGetMagneticState();

    public native int nativeGetNearThreshold();

    public native int nativeGetOLEDBrightness();

    public native int nativeGetPowerMode();

    public native int nativeGetPowerSaveSleepTime();

    public native int nativeGetPowerSaveSleepTimeLevel();

    public native int nativeGetPowerSaveSleepTimeLevelCount();

    public native String nativeGetProductWrapBoxSN();

    public native int nativeGetProximityFarThreshold();

    public native int nativeGetProximityNearThreshold();

    public native int nativeGetProximityValue();

    public native int nativeGetProximityWearingState();

    public native int nativeGetPsensorIsWearing();

    public native int nativeGetPsensorSwitchState();

    public native int nativeGetPsensorValue();

    public native int nativeGetRgbCameraPluginState();

    public native int nativeGetRgbCameraState();

    public native int nativeGetScreenColorCalibrationType();

    public native int nativeGetScreenStatus();

    public native int nativeGetSleepTime();

    public native int nativeGetStorageAvailable();

    public native int nativeGetStorageMode();

    public native long nativeGetSupportedDisplayModeMask();

    public native int nativeGetTemperature();

    public native int nativeGetTemperatureData(int i);

    public native int nativeGetTemperatureLevel();

    public native int nativeGetTemperatureState();

    public native int nativeGetTemperatureStateProcessEnable(int i);

    public native float nativeGetTemperatureValue(int i);

    public native String nativeGetVersion();

    public native int nativeGetVsyncState();

    public native int nativeGetWorldLEDState();

    public native int nativeIsPowerSaveEnable();

    public native int nativeIsProximityEnable();

    public native boolean nativeRebootGlasses();

    public native boolean nativeRebootSony();

    public native boolean nativeRecenterGlasses();

    public native boolean nativeResetOv580();

    public native boolean nativeSendPrivilegedActivation(int i);

    public native boolean nativeSet2D3DMode(int i);

    public native boolean nativeSetActivatedState(int i);

    public native boolean nativeSetActivationTime(long j);

    public native boolean nativeSetAudioAlgorithm(int i);

    public native boolean nativeSetBrightness(int i);

    public native boolean nativeSetBrightnessSet(int i);

    public native boolean nativeSetDPESDParam(int i);

    public native boolean nativeSetDPHDCPEnable(int i);

    public native boolean nativeSetDPLevel(int i);

    public native boolean nativeSetDisplayBrightnessLevel(int i);

    public native boolean nativeSetDisplayBypassPsensorFlag(int i);

    public native boolean nativeSetDisplayColorCalibrationType(int i);

    public native boolean nativeSetDisplayColorTemperatureLevel(int i);

    public native boolean nativeSetDisplayDefaultResolution(int i);

    public native boolean nativeSetDisplayDefaultStartMode(int i);

    public native boolean nativeSetDisplayDutyValue(int i);

    public native boolean nativeSetDisplayLuminanceValue(int i);

    public native boolean nativeSetDisplayMapParams(NRGlassesControlParams nRGlassesControlParams);

    public native boolean nativeSetDisplayPermitScreenEnable(int i);

    public native boolean nativeSetDisplayScreenEnable(int i, int i2);

    public native boolean nativeSetDisplayState(int i);

    public native boolean nativeSetDpCurrentEdid(int i);

    public native boolean nativeSetDpDataFilterMode(int i);

    public native boolean nativeSetDpHDCPEnable(int i);

    public native boolean nativeSetDpInputMode(int i);

    public native boolean nativeSetDpWorkingMode(int i);

    public native boolean nativeSetDuty(int i);

    public native boolean nativeSetEcLevel(int i);

    public native boolean nativeSetEcValue(int i, int i2);

    public native boolean nativeSetElectrochromicLevel(int i);

    public native boolean nativeSetElectrochromicValue(NRGlassesControlParams nRGlassesControlParams);

    public native boolean nativeSetEnablePhysicalButtonSwitchDisplayModeFlag(int i);

    public native boolean nativeSetFarThreshold(int i);

    public native boolean nativeSetGlassesNetLogEnable(int i);

    public native boolean nativeSetGlassesSceneMode(int i);

    public native boolean nativeSetGlassesSpaceMode(int i);

    public native boolean nativeSetGlassesUltraWideEnable(int i);

    public native boolean nativeSetHostID(String str);

    public native boolean nativeSetHostType(int i);

    public native boolean nativeSetIMUFrequencyDivider(int i);

    public native boolean nativeSetLedRgbMode(NRGlassesControlParams nRGlassesControlParams);

    public native boolean nativeSetLedWorkMode(int i);

    public native boolean nativeSetLightIntensityState(int i);

    public native boolean nativeSetLogTrigger(int i);

    public native boolean nativeSetMagneticState(int i);

    public native boolean nativeSetNearThreshold(int i);

    public native boolean nativeSetOLEDBrightness(int i);

    public native boolean nativeSetPowerMode(int i);

    public native boolean nativeSetPowerSaveEnable(int i);

    public native boolean nativeSetPowerSaveSleepTime(int i);

    public native boolean nativeSetPowerSaveSleepTimeLevel(int i);

    public native boolean nativeSetProximityEnable(int i);

    public native boolean nativeSetProximityFarThreshold(int i);

    public native boolean nativeSetProximityNearThreshold(int i);

    public native boolean nativeSetPsensorSwitchState(int i);

    public native boolean nativeSetRgbCameraState(int i);

    public native boolean nativeSetScreenColorCalibrationType(int i);

    public native boolean nativeSetSleepTime(int i);

    public native boolean nativeSetStorageMode(int i);

    public native boolean nativeSetTemperatureState(int i);

    public native boolean nativeSetTemperatureStateProcessEnable(int i, int i2);

    public native boolean nativeSetUpdateFlag();

    public native boolean nativeSetVsyncState(int i);

    public native boolean nativeSetWorldLEDState(int i);

    public native boolean nativeStartErrorsAndEventsReport();

    public native boolean nativeStartGlassesEventsReport(int i);

    public native boolean nativeStopGlassesEventsReport(int i);

    public native boolean nativeToggleKey(int i, int i2, int i3);

    private Control() {
    }

    public static Control getInstance() {
        if (sInstance == null) {
            synchronized (Control.class) {
                if (sInstance == null) {
                    sInstance = new Control();
                }
            }
        }
        return sInstance;
    }

    public static void onGlassesAction(int i, int i2, int i3, long j) {
        INRControlCallback iNRControlCallback = sCallback;
        if (iNRControlCallback != null) {
            iNRControlCallback.onGlassesAction(i, i2, i3, j);
        }
    }

    public static void onGlassesEvent(int i, int i2, int i3, int i4, int i5, byte[] bArr) {
        INRControlCallback iNRControlCallback = sCallback;
        if (iNRControlCallback != null) {
            iNRControlCallback.onGlassesEvent(i, i2, i3, i4, i5, bArr);
        }
    }

    public static void onClientCountChanged(int i, int i2) {
        INRControlCallback iNRControlCallback = sCallback;
        if (iNRControlCallback != null) {
            iNRControlCallback.onClientCountChanged(i, i2);
        }
    }

    public static void setINRControlCallback(INRControlCallback iNRControlCallback) {
        sCallback = iNRControlCallback;
    }

    public static void setImuCallback(ImuAccAndGyroCallback imuCallback) {
        Control.imuCallback = imuCallback;
    }

    public static void setMagCallback(ImuMagCallback magCallback) {
        Control.magCallback = magCallback;
    }

    public interface ImuAccAndGyroCallback {
        void onImuAccAndGyroData(long t1, long t2, float gx, float gy, float gz, float az, float ay, float ax);
    }

    public interface ImuMagCallback {
        void onImuMagData(long t1, long t2, float mx, float my, float mz);
    }

    public static void onImuAccAndGyroData(long t1, long t2, float gx, float gy, float gz, float az, float ay, float ax) {
        if (imuCallback != null) {
            imuCallback.onImuAccAndGyroData(t1, t2, gx, gy, gz, az, ay, ax);
        }
    }

    public static void onImuMagData(long t1, long t2, float mx, float my, float mz) {
        if (magCallback != null) {
            magCallback.onImuMagData(t1, t2, mx, my, mz);
        }
    }
}
