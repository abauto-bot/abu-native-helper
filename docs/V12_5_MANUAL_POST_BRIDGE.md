# ABU Native Helper V12.5 — APK to Web Manual POST Bridge

## Goal

Create a safe bridge between APK pairing JSON and the private Web Node pairing API.

## Added

- Manual POST Bridge UI section
- Prepare Manual POST Command button
- Share Manual POST Command button
- Clear Manual POST Draft button
- Core model: AbuManualPostBridge
- Version: 1.2.5-v12.5

## Important Safety Boundary

The APK does not make any HTTP request.
The APK does not store private portal Basic Auth credentials.
The APK does not receive any token.
The APK does not execute commands.

The user manually posts the generated JSON/curl command through a trusted private channel.

## Endpoint

POST https://ai.ecoluup.com/private/api/device/pair/request

## Next

V12.6 will add confirm/deny boundary UI foundation.
