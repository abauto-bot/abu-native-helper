# ABU Native Helper V12.0 — Full Beta Development Sprint

## Goal

Stop tiny install-by-install development.
Build one stronger beta APK after source-level development is complete.

## Scope

### Phone APK

- Clean ABU Native Helper beta shell
- Phone identity section
- Pairing section
- Language mode section
- Device status section
- Safe command inbox UI foundation
- Local audit log foundation
- Web portal shortcut section
- No dangerous permission enabled

### Web Node / VPS

- Pairing API skeleton
- Device registry design
- Audit event design
- Confirm / deny boundary
- Token issue boundary
- Kasm protected
- GOVO protected

## Safety Rules

- No root
- No hidden screen capture
- No microphone permission
- No active accessibility control
- No notification listener enable
- No device admin
- No default launcher replacement
- No server execution token in APK
- No GOVO/Kasm touch without explicit scope

## Final Output

One GitHub Actions green build:
- abu-native-helper-debug-apk

User installs only final beta APK.
