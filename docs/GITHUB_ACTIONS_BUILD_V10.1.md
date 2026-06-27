# ABU Native Helper V10.1 — GitHub Actions APK Build

## Why GitHub Actions

Termux readiness check showed:
- gradlew missing
- ANDROID_HOME empty
- ANDROID_SDK_ROOT empty

So the recommended first debug APK build path is GitHub Actions or Android Studio.

## Workflow

File:
.github/workflows/android-debug-apk.yml

What it does:
1. Checkout project
2. Setup JDK 17
3. Setup Gradle 8.10.2
4. Run safety scan
5. Build debug APK
6. Upload APK artifact

## Safety Scan Blocks

The workflow stops if active dangerous strings are found:
- RECORD_AUDIO
- MediaProjectionManager
- ImageReader
- dispatchGesture
- getRootInActiveWindow
- android.intent.category.HOME
- DEVICE_ADMIN

## Build Command

gradle :app:assembleDebug --stacktrace

## Install Rule

Build first.
Install second.
Enable permissions last.
Dangerous layers remain disabled.
