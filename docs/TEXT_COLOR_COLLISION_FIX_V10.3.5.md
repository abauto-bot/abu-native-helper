# V10.3.5 Text Color Collision Fix

GitHub Actions failed at compileDebugKotlin:

MainActivity.kt:
- setTextColor(...) had invalid argument at two lines.

Cause:
- Class had color variable named `text`.
- Inside Android view apply blocks, `text` resolves to the view text property, not the color Int.
- Kotlin passed Editable/CharSequence instead of Int color.

Fix:
- Renamed color variable to `textColor`.
- Replaced color usages accordingly.

Expected:
- compileDebugKotlin passes.
- assembleDebug creates debug APK.
