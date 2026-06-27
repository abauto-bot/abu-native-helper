# ABU Native Helper

ABU Native Helper is the Android-side Phone Node bridge for ABU OS.

Package:
com.abuos.nativehelper

Current version:
V9.1 Project Skeleton

Current screen:
- ABU Native Helper
- Pair Device
- Phone Status
- Pairing
- Safety Policy

Execution:
Disabled / foundation only.

Security:
- No root
- No factory reset
- No hidden screenshot
- No silent spying
- No dangerous execution without audit + confirmation

Next:
V9.2 Pair Device screen + local identity foundation.

## V10.1 Build Path

Termux local build is not recommended yet because Android SDK / Gradle wrapper are missing.

Recommended:
- GitHub Actions workflow:
  .github/workflows/android-debug-apk.yml

Build:
- Push project to GitHub.
- Run workflow: Build ABU Native Helper Debug APK.
- Download artifact: abu-native-helper-debug-apk.

Safety:
- No install until user confirms.
- Dangerous layers remain disabled.
