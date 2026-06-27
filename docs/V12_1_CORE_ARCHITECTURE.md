# ABU Native Helper V12.1 — Core Architecture Foundation

## Purpose

V12.1 stops patching everything directly inside MainActivity and starts a cleaner beta architecture.

## Added Source Modules

- `AbuSafetyPolicy`
- `DeviceIdentity`
- `PhoneStatusSnapshot`
- `PairingRequestDraft`
- `AbuAuditEvent`
- `AbuCommandInboxItem`
- `AbuCommandPolicy`
- `AbuBetaArchitecture`

## Current State

This is still safe beta foundation work.

No install required at this step.
No server execution.
No active command execution.
No token stored in APK.

## Safety

Blocked by policy:

- root execution
- hidden screen capture
- microphone recording
- active accessibility control
- notification listener enable
- device admin
- default launcher replacement
- silent install
- factory reset
- server execution token inside APK

## Next

V12.2 will add private Web Node pairing API skeleton on VPS side.
