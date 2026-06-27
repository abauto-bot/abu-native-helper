# ABU Native Helper V9.5 — Notification Listener Foundation

## Added

- AbuNotificationListener.kt skeleton
- Manifest service declaration
- Service intentionally disabled:
  android:enabled="false"
- Notification Access settings shortcut
- Local safety explanation in app UI

## Important

V9.5 cannot read notifications.

Why:
- The service is disabled in manifest.
- User has not granted Notification Access.
- No server sync exists.
- No execution token exists.

## Future Requirements

Before notification reading is enabled:
1. Explicit user approval
2. Android Notification Access manual permission
3. ABU OS audit event
4. Risk classification
5. Policy confirmation
6. No auto-reply without explicit confirmation

## Blocked

- Auto reply
- Hidden notification collection
- Background spying
- Server sync
- Message sending
- GOVO production actions
- Kasm modification

## Next

V9.6:
Voice Button + Telegram Routing foundation.
