# ABU Native Helper V10.2 — GitHub Push + APK Build Run

## Goal

Push ABU Native Helper to GitHub and trigger GitHub Actions debug APK build.

## Safety

No APK install yet.
No permissions enabled.
Notification Listener remains disabled.
Accessibility Service remains disabled.
No screen capture.
No microphone permission.
No default launcher replacement.

## Build Trigger

Workflow:
.github/workflows/android-debug-apk.yml

Trigger:
- push to main/master
- manual workflow_dispatch

## After Push

Open GitHub repo:
Actions → Build ABU Native Helper Debug APK

Expected artifact:
abu-native-helper-debug-apk

## Install Rule

Build first.
Download second.
Install only after user confirms.
Enable permissions last.
