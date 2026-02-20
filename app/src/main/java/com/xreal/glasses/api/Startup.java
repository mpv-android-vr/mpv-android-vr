package com.xreal.glasses.api;

import android.content.Context;

public class Startup {
    public static native void nativeDestroyService();

    public static native void nativeForceQuitApps(int i);

    public static native int nativeGetApiErrorCode();

    public static native String nativeGetNRSDKVersion();

    public static native Object[] nativeGetPropertyKeys();

    public static native void nativeGlassesDeInit();

    public static native boolean nativeGlassesInit();

    public static native boolean nativeGlassesPause();

    public static native boolean nativeGlassesResume();

    public static native void nativeGraycameraDeInit();

    public static native boolean nativeGraycameraInit();

    public static native boolean nativeGraycameraPause();

    public static native boolean nativeGraycameraResume();

    public static native void nativeImuDeInit();

    public static native boolean nativeImuInit();

    public static native boolean nativeImuPause();

    public static native boolean nativeImuResume();

    public static native void nativeInitService(Context context);

    public static native void nativeInitSetForegroundService(boolean z);

    public static native int nativeLoadDeviceConfig();

    public static native void nativeSetControllerTrackingConfig(String str);

    public static native void nativeSetJavaLibraryPath(String str);

    public static native void nativeSetNativeLibraryPath(String str);

    public static native void nativeSetPerceptionRuntimeConfig(String str);

    public static native void nativeSetPerceptionRuntimeId(int i);

    public static native void nativeSetPropertyFlag(String str, int i);

    public static native void nativeSetServiceMode(int i);

    public static native int nativeSetUserProfile(String str);

    public static native void nativeStartDeviceLog();

    public static native void nativeStartService();

    public static native void nativeStopDeviceLog();

    public static native void nativeStopService();
}
