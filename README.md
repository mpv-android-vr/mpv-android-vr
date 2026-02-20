# mpv VR for Android

A Proof of concept VR Android video player that supports Xreal One Pro head tracking.

**This is not production ready, it's just a POC that works.**

It is meant to be an inspiration for others to refine and further develop a better app.

## Proof of concept APK

[**Download APK**](https://github.com/mpv-android-vr/mpv-android-vr/releases/download/1/mpv-vr-arm64-v8a-release-v1.apk)

![App Screenshot](https://github.com/user-attachments/assets/847092e6-31a9-446c-9236-bb5767375a20)

## What's working?

- Projections:
  - equirectangular
  - dual fisheye
  - dual half equirectangular
  - half equirectangular
  - dual vert_equirectangular
  - cylindrical
  - equiangular cubemap
  - dual equiangular cubemap
- Eye outputs:
  - left
  - right 
  - half sbs
  - full sbs
- Camera controls:
  - Touch
  - Xreal One Pro head tracking 
  - FOV/Fish eye FOV controls

## What's this?

I've put together multiple projects to create a POC Xreal Android VR player:

- https://github.com/mpv-android/mpv-android 
- https://github.com/kasper93/mpv360
- https://www.reddit.com/r/Xreal/comments/1plx34z/xreal_one_android_sdk_working_without_unity_or
- https://github.com/SamiMitwalli/One-Pro-IMU-Retriever-Demo

And I've done some adjustments:

- adapted mpv360.glsl shader to work on Android
- implemented Kotlin controller for the mpv360 shader (removed the lua scripting)
- added VR specific buttons to the MPV Video player
- implemented Xreal One Pro head tracking controls based on Xreal Framework
- implemented a basic smoothing algorithm for the Xreal IMU data for head tracking

## How to compile and run

Import the project in Android Studio and run it as a normal Android app. 

I've included the precompiled binaries for **arm64-v8a**. If you want to build them yourself, you have to check the guide from MPV Android: https://github.com/mpv-android/mpv-android/tree/master/buildscripts

I've included also the Xreal framework.jar nrcontroller.jar sparrow.jar files from their APK. 

## Tags / Keywords

Android VR XR SBS 3D 180 360 video player with support for Xreal One / Xreal One Pro head tracking IMU data

Android Xreal VR video player

Android 3D VR video player

Android 180 video player
