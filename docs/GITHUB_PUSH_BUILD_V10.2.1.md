# ABU Native Helper V10.2.1 — Fixed Safety Scan

The first V10.2 push script stopped because docs mention blocked terms like RECORD_AUDIO and MediaProjectionManager as safety warnings.

Fix:
- Active safety scan now checks only app/src/main
- Docs are excluded because they intentionally describe blocked features
- Notification and Accessibility services remain disabled
- No active microphone, screen capture, accessibility control, or default launcher replacement
