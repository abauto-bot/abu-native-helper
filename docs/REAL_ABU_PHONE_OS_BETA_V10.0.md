# ABU Native Helper V10.0 — Real ABU Phone OS Beta Readiness

## Current State

ABU Native Helper is now a safe Android Phone Node foundation.

Completed:
- V9.1 Android project skeleton
- V9.2 Pair Device + Local Identity
- V9.3 Phone Status Model
- V9.4 App Open Allowlist
- V9.5 Notification Listener Foundation disabled
- V9.6 Voice Draft + Telegram Routing
- V9.7 Screen Vision Permission Foundation
- V9.8 Accessibility Service Disabled Skeleton
- V9.9 ABU Launcher Shell Foundation

## Current Safety

No dangerous active control:
- No root
- No factory reset
- No hidden screenshot
- No MediaProjection
- No RECORD_AUDIO
- No active Accessibility control
- No notification read
- No auto-send
- No server execution token
- No default launcher replacement

## Beta Build Options

### Option A — Android Studio
Best for first APK build.

Steps:
1. Open project folder:
   ~/abu-native-helper
2. Let Gradle sync.
3. Build debug APK.
4. Install manually on phone.
5. Test UI only.

### Option B — GitHub Actions
Good for repeatable cloud APK build.

Need:
- GitHub repo
- Android Gradle plugin
- workflow yaml
- debug APK artifact

### Option C — Termux local build
Possible only if Java, Gradle, and Android SDK are correctly available.
Often harder on Android.

## V10.1 Recommended Next

Prepare Android Studio / GitHub build path:
- create gradle wrapper if missing
- validate compileSdk
- create build command
- generate debug APK
- do not install until user confirms

## Beta Rule

Build first.
Install second.
Enable permissions last.
Dangerous layers remain disabled.
