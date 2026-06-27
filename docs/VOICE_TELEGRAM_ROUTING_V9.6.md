# ABU Native Helper V9.6 — Voice Button + Telegram Routing Foundation

## Added

- Voice command draft text box
- Save Voice Draft button
- Route Draft to Telegram button
- Clear Voice Draft button
- Local voice routing state display

## Important

This is not real microphone voice yet.

V9.6 does not request:
- RECORD_AUDIO
- always listening
- wake word
- speech recognition service

Telegram routing:
- Uses Android ACTION_SEND share intent
- User must manually choose/send to Hi_Govo_bot
- No bot token inside APK
- No auto-send

## Security

Blocked:
- Always listening
- Background recording
- Auto sending message
- Bot token in APK
- Server command execution
- Hidden action

## Next

V9.7:
Screen Vision Permission Foundation:
- permission explanation
- no screenshot capture yet
- MediaProjection model only after explicit user approval
