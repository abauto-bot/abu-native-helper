package com.abuos.nativehelper

import android.app.Activity
import android.os.Bundle
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.BatteryManager
import android.provider.Settings
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MainActivity : Activity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var statusText: TextView
    private lateinit var localIdText: TextView
    private lateinit var pairInput: EditText
    private lateinit var phoneStatusText: TextView
    private lateinit var appStatusText: TextView
    private lateinit var notificationStatusText: TextView
    private lateinit var voiceInput: EditText
    private lateinit var voiceStatusText: TextView
    private lateinit var screenVisionStatusText: TextView
    private lateinit var accessibilityStatusText: TextView
    private lateinit var launcherStatusText: TextView
    private lateinit var languageStatusText: TextView
    private lateinit var pairingSyncStatusText: TextView
    private lateinit var pairingApiStatusText: TextView

    private val bg = Color.rgb(3, 7, 5)
    private val cardBg = Color.rgb(8, 25, 18)
    private val green = Color.rgb(81, 242, 139)
    private val gold = Color.rgb(244, 199, 107)
    private val textColor = Color.rgb(238, 251, 243)
    private val muted = Color.rgb(157, 181, 168)

    data class AllowedApp(
        val label: String,
        val packageName: String,
        val risk: String = "low"
    )

    private val allowedApps = listOf(
        AllowedApp("Telegram", "org.telegram.messenger"),
        AllowedApp("WhatsApp", "com.whatsapp"),
        AllowedApp("YouTube", "com.google.android.youtube"),
        AllowedApp("Google Maps", "com.google.android.apps.maps"),
        AllowedApp("Chrome", "com.android.chrome"),
        AllowedApp("Gmail", "com.google.android.gm")
    )


    private fun rounded(color: Int, radius: Float = 28f): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
            setStroke(1, Color.argb(60, 255, 255, 255))
        }
    }

    private fun applyBlockMargin(view: android.view.View, top: Int = 10, bottom: Int = 10) {
        view.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, top, 0, bottom)
        }
    }

    private fun tv(value: String, size: Float, color: Int, bold: Boolean = false): TextView {
        return TextView(this).apply {
            this.text = value
            textSize = size
            setTextColor(color)
            setPadding(24, 10, 24, 10)
            if (bold) typeface = Typeface.DEFAULT_BOLD
        }
    }

    private fun card(title: String, body: String): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 18, 20, 18)
            applyBlockMargin(this, 10, 10)
            background = rounded(cardBg, 30f)
            addView(tv(title, 18f, green, true))
            addView(tv(body, 14f, muted))
        }
    }

    private fun nowIso(): String {
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        return fmt.format(Date())
    }

    private fun getOrCreateLocalId(): String {
        val existing = prefs.getString("local_phone_id", null)
        if (!existing.isNullOrBlank()) return existing

        val newId = "ABU-PHONE-" + UUID.randomUUID().toString()
            .replace("-", "")
            .take(10)
            .uppercase(Locale.US)

        prefs.edit()
            .putString("local_phone_id", newId)
            .putString("local_id_created_at", nowIso())
            .apply()

        return newId
    }

    private fun formatBytes(bytes: Long): String {
        val gb = bytes.toDouble() / 1024.0 / 1024.0 / 1024.0
        return String.format(Locale.US, "%.2f GB", gb)
    }

    private fun getStorageStatus(): String {
        return try {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val total = stat.blockCountLong * stat.blockSizeLong
            val free = stat.availableBlocksLong * stat.blockSizeLong
            val used = total - free
            val usedPct = if (total > 0) (used.toDouble() / total.toDouble()) * 100 else 0.0

            """
Storage:
- Total: ${formatBytes(total)}
- Free: ${formatBytes(free)}
- Used: ${formatBytes(used)} (${String.format(Locale.US, "%.1f", usedPct)}%)
""".trimIndent()
        } catch (e: Exception) {
            "Storage: unavailable (${e.message})"
        }
    }

    private fun getBatteryStatus(): String {
        return try {
            val batteryIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val status = batteryIntent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1

            val pct = if (level >= 0 && scale > 0) (level * 100 / scale) else -1
            val charging = when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> "charging"
                BatteryManager.BATTERY_STATUS_FULL -> "full"
                BatteryManager.BATTERY_STATUS_DISCHARGING -> "discharging"
                BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "not charging"
                else -> "unknown"
            }

            """
Battery:
- Level: ${if (pct >= 0) "$pct%" else "unknown"}
- State: $charging
""".trimIndent()
        } catch (e: Exception) {
            "Battery: unavailable (${e.message})"
        }
    }

    private fun getDeviceStatus(): String {
        return """
Device:
- Manufacturer: ${Build.MANUFACTURER}
- Model: ${Build.MODEL}
- Brand: ${Build.BRAND}
- Device: ${Build.DEVICE}
- Android: ${Build.VERSION.RELEASE}
- SDK: ${Build.VERSION.SDK_INT}
- App Version: V11.0
""".trimIndent()
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getLaunchIntentForPackage(packageName) != null
        } catch (e: Exception) {
            false
        }
    }

    private fun openAllowedApp(app: AllowedApp) {
        val allowed = allowedApps.any { it.packageName == app.packageName }
        if (!allowed) {
            Toast.makeText(this, "Blocked: app not allowlisted", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = packageManager.getLaunchIntentForPackage(app.packageName)
        if (intent == null) {
            Toast.makeText(this, "${app.label} not installed or not visible", Toast.LENGTH_SHORT).show()
            refreshAppStatus()
            return
        }

        prefs.edit()
            .putString("last_app_open_request", app.label)
            .putString("last_app_open_package", app.packageName)
            .putString("last_app_open_at", nowIso())
            .apply()

        startActivity(intent)
    }

    private fun openAndroidSettings() {
        prefs.edit()
            .putString("last_app_open_request", "Android Settings")
            .putString("last_app_open_package", "android.settings.SETTINGS")
            .putString("last_app_open_at", nowIso())
            .apply()

        startActivity(Intent(Settings.ACTION_SETTINGS))
    }

    private fun refreshAppStatus() {
        val lines = mutableListOf<String>()
        lines.add("Allowlist status:")
        for (app in allowedApps) {
            val installed = if (isAppInstalled(app.packageName)) "installed" else "not installed / not visible"
            lines.add("- ${app.label}: $installed")
        }

        lines.add("")
        lines.add("Last open request:")
        lines.add("- App: ${prefs.getString("last_app_open_request", "none")}")
        lines.add("- Package: ${prefs.getString("last_app_open_package", "none")}")
        lines.add("- Time: ${prefs.getString("last_app_open_at", "never")}")
        lines.add("")
        lines.add("Policy:")
        lines.add("- Only allowlisted apps")
        lines.add("- Local button action only")
        lines.add("- No server command")
        lines.add("- No hidden action")

        appStatusText.text = lines.joinToString("\n")
    }

    private fun openNotificationAccessSettings() {
        prefs.edit()
            .putString("last_notification_settings_opened_at", nowIso())
            .apply()

        try {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        } catch (e: Exception) {
            Toast.makeText(this, "Notification Access settings unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshNotificationFoundation() {
        val lastOpened = prefs.getString("last_notification_settings_opened_at", "never")

        notificationStatusText.text = """
Notification Listener Foundation:
- Service class: AbuNotificationListener
- Manifest status: disabled intentionally
- Notification read: disabled
- Auto reply: blocked
- Server sync: disabled
- Last settings opened: $lastOpened

Future requirement:
1. User manually enables Notification Access
2. ABU OS creates audit event
3. Risk classifier approves safe notification read
4. No auto-reply without explicit confirmation

V9.6 policy:
This app cannot read notifications yet because the service is disabled.
""".trimIndent()
    }

    private fun saveVoiceDraft() {
        val raw = voiceInput.text.toString().trim()
        if (raw.length < 2) {
            Toast.makeText(this, "Write a command draft first", Toast.LENGTH_SHORT).show()
            return
        }

        if (raw.length > 500) {
            Toast.makeText(this, "Draft too long", Toast.LENGTH_SHORT).show()
            return
        }

        prefs.edit()
            .putString("last_voice_draft", raw)
            .putString("last_voice_draft_at", nowIso())
            .putString("voice_routing_state", "draft_saved_local_only")
            .apply()

        refreshVoiceFoundation()
        Toast.makeText(this, "Voice draft saved locally", Toast.LENGTH_SHORT).show()
    }

    private fun clearVoiceDraft() {
        prefs.edit()
            .remove("last_voice_draft")
            .putString("last_voice_draft_at", "never")
            .putString("voice_routing_state", "no_draft")
            .apply()

        voiceInput.setText("")
        refreshVoiceFoundation()
        Toast.makeText(this, "Voice draft cleared", Toast.LENGTH_SHORT).show()
    }

    private fun routeDraftToTelegram() {
        val draft = voiceInput.text.toString().trim()
            .ifBlank { prefs.getString("last_voice_draft", "") ?: "" }
            .trim()

        if (draft.length < 2) {
            Toast.makeText(this, "No voice draft to route", Toast.LENGTH_SHORT).show()
            return
        }

        val msg = """
ABU Voice Draft:
$draft

Route:
User must manually choose Hi_Govo_bot and press send.

Safety:
No auto-send. No bot token. No background execution.
""".trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, msg)
            setPackage("org.telegram.messenger")
        }

        try {
            prefs.edit()
                .putString("voice_routing_state", "telegram_share_intent_opened")
                .putString("last_voice_route_at", nowIso())
                .apply()

            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Telegram not installed or share unavailable", Toast.LENGTH_SHORT).show()
        }

        refreshVoiceFoundation()
        refreshScreenVisionFoundation()
        refreshAccessibilityFoundation()
        refreshLauncherShell()
        refreshLanguageFoundation()
        refreshPairingSyncDesign()
        refreshPairingApiBoundary()
    }

    private fun refreshVoiceFoundation() {
        val draft = prefs.getString("last_voice_draft", "") ?: ""
        val draftAt = prefs.getString("last_voice_draft_at", "never")
        val routeAt = prefs.getString("last_voice_route_at", "never")
        val state = prefs.getString("voice_routing_state", "no_draft")

        voiceStatusText.text = """
Voice Button Foundation:
- Mic permission: not requested
- Always listening: blocked
- Speech recognition: not active
- Telegram route: manual share only
- Auto-send: blocked
- Bot token inside APK: no
- Routing state: $state
- Last draft at: $draftAt
- Last route at: $routeAt

Last local draft:
${if (draft.isBlank()) "none" else draft}

Future:
V9.7 may add speech-to-text permission request only after explicit approval.
""".trimIndent()
    }


    private fun openDisplaySettingsForScreenVision() {
        prefs.edit()
            .putString("last_screen_vision_settings_opened_at", nowIso())
            .apply()

        try {
            startActivity(Intent(Settings.ACTION_DISPLAY_SETTINGS))
        } catch (e: Exception) {
            Toast.makeText(this, "Display settings unavailable", Toast.LENGTH_SHORT).show()
        }

        refreshScreenVisionFoundation()
    }

    private fun refreshScreenVisionFoundation() {
        val lastOpened = prefs.getString("last_screen_vision_settings_opened_at", "never")

        screenVisionStatusText.text = """
Screen Vision Foundation:
- Screenshot capture: disabled
- MediaProjection: not requested
- OCR/UI detection: planned only
- Hidden screen read: blocked
- Server sync: disabled
- Last settings opened: $lastOpened

Future requirement:
1. User manually starts screen capture permission
2. ABU OS creates audit event
3. Risk classifier marks the request
4. Confirmation is required
5. Capture session must be visible and stoppable

V9.7 policy:
This app cannot capture the screen yet. It only documents the future permission flow.
""".trimIndent()
    }


    private fun openAccessibilitySettingsForAbu() {
        prefs.edit()
            .putString("last_accessibility_settings_opened_at", nowIso())
            .apply()

        try {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        } catch (e: Exception) {
            Toast.makeText(this, "Accessibility settings unavailable", Toast.LENGTH_SHORT).show()
        }

        refreshAccessibilityFoundation()
    }

    private fun refreshAccessibilityFoundation() {
        val lastOpened = prefs.getString("last_accessibility_settings_opened_at", "never")

        accessibilityStatusText.text = """
Accessibility Prototype:
- Service class: AbuAccessibilityService
- Manifest status: disabled intentionally
- Tap: disabled
- Scroll: disabled
- Type: disabled
- Screen content read: disabled
- Gestures: disabled
- Server sync: disabled
- Last settings opened: $lastOpened

Future requirement:
1. User manually enables Accessibility
2. ABU OS creates audit event
3. Risk classifier marks action type
4. Exact confirmation phrase required
5. Emergency stop must exist
6. Only allowlisted actions can run

V9.8 policy:
This app cannot control the screen yet. It only declares a disabled prototype.
""".trimIndent()
    }





    private fun buildPairingApiJson(): String {
        val localId = getOrCreateLocalId()
        val pairingCode = prefs.getString("pairing_code", "") ?: ""
        val language = currentLanguage()

        return """
{
  "api_version": "v11.0",
  "mode": "pairing_request_draft_only",
  "endpoint_future": "https://ai.ecoluup.com/api/device/pair/request",
  "phone_node": {
    "local_phone_id": "$localId",
    "pairing_code": "${if (pairingCode.isBlank()) "not_set" else pairingCode}",
    "app_package": "com.abuos.nativehelper",
    "app_version": "1.1.0-v11.0",
    "language_mode": "$language"
  },
  "security": {
    "server_call": "disabled",
    "real_token": "not_issued",
    "execution": "disabled",
    "background_sync": "blocked",
    "notification_access": "disabled",
    "accessibility": "disabled",
    "screen_capture": "disabled"
  },
  "requested_at": "${nowIso()}"
}
""".trimIndent()
    }

    private fun preparePairingApiDraft() {
        val draft = buildPairingApiJson()

        prefs.edit()
            .putString("pairing_api_state", "api_request_draft_saved_local_only")
            .putString("pairing_api_draft", draft)
            .putString("pairing_api_draft_at", nowIso())
            .apply()

        refreshPairingApiBoundary()
        Toast.makeText(this, "Pairing API draft saved locally", Toast.LENGTH_SHORT).show()
    }

    private fun sharePairingApiDraft() {
        val draft = prefs.getString("pairing_api_draft", "") ?: ""
        val payload = if (draft.isBlank()) buildPairingApiJson() else draft

        val msg = """
ABU Native Helper Pairing API Draft

$payload

Safety:
This is manual share only.
No server call from APK.
No token issued.
No execution enabled.
""".trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, msg)
        }

        try {
            prefs.edit()
                .putString("pairing_api_state", "manual_share_intent_opened")
                .putString("pairing_api_shared_at", nowIso())
                .apply()

            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Share unavailable", Toast.LENGTH_SHORT).show()
        }

        refreshPairingApiBoundary()
    }

    private fun clearPairingApiDraft() {
        prefs.edit()
            .putString("pairing_api_state", "no_draft")
            .putString("pairing_api_draft_at", "never")
            .remove("pairing_api_draft")
            .apply()

        refreshPairingApiBoundary()
        Toast.makeText(this, "Pairing API draft cleared", Toast.LENGTH_SHORT).show()
    }

    private fun refreshPairingApiBoundary() {
        val state = prefs.getString("pairing_api_state", "no_draft")
        val draftAt = prefs.getString("pairing_api_draft_at", "never")
        val sharedAt = prefs.getString("pairing_api_shared_at", "never")
        val localId = getOrCreateLocalId()
        val pairingCode = prefs.getString("pairing_code", "") ?: ""

        pairingApiStatusText.text = """
Pairing API Boundary:
- Future endpoint: /api/device/pair/request
- ABU Phone ID: $localId
- Pairing code: ${if (pairingCode.isBlank()) "not set" else pairingCode}
- API draft state: $state
- Draft saved at: $draftAt
- Manual shared at: $sharedAt
- Server call: disabled
- Real token: not issued
- Execution: disabled

V11.0 contract:
APK can prepare pairing JSON locally.
APK cannot call server yet.
Server must create audit event before real device token.

Next:
V11.1 will create private Web Node pairing API skeleton.
""".trimIndent()
    }

    private fun preparePairingSyncDraft() {
        val localId = getOrCreateLocalId()
        val pairingCode = prefs.getString("pairing_code", "") ?: ""

        prefs.edit()
            .putString("pairing_sync_state", "draft_saved_local_only")
            .putString("pairing_sync_local_id", localId)
            .putString("pairing_sync_code_preview", if (pairingCode.isBlank()) "not_set" else pairingCode)
            .putString("pairing_sync_draft_at", nowIso())
            .apply()

        refreshPairingSyncDesign()
        Toast.makeText(this, "Pairing sync draft saved locally", Toast.LENGTH_SHORT).show()
    }

    private fun clearPairingSyncDraft() {
        prefs.edit()
            .putString("pairing_sync_state", "no_draft")
            .putString("pairing_sync_draft_at", "never")
            .remove("pairing_sync_code_preview")
            .apply()

        refreshPairingSyncDesign()
        Toast.makeText(this, "Pairing sync draft cleared", Toast.LENGTH_SHORT).show()
    }

    private fun refreshPairingSyncDesign() {
        val localId = getOrCreateLocalId()
        val pairingCode = prefs.getString("pairing_code", "") ?: ""
        val syncState = prefs.getString("pairing_sync_state", "no_draft")
        val draftAt = prefs.getString("pairing_sync_draft_at", "never")

        pairingSyncStatusText.text = """
Pairing Sync Design:
- ABU Phone ID: $localId
- Pairing code: ${if (pairingCode.isBlank()) "not set" else pairingCode}
- Sync state: $syncState
- Draft saved at: $draftAt
- Server sync: disabled
- Real token: not issued
- Background sync: blocked
- Execution: disabled

Future V11 flow:
1. APK sends safe pairing request to ABU Web Node
2. Web Node creates audit event
3. Telegram/Private portal confirms device
4. Server issues limited device token
5. Phone can report safe status only

V10.9 policy:
This is only a local sync design foundation. No network request is made.
""".trimIndent()
    }

    private fun currentLanguage(): String {
        return prefs.getString("language_mode", "en") ?: "en"
    }

    private fun label(en: String, bn: String): String {
        return if (currentLanguage() == "bn") bn else en
    }

    private fun toggleLanguageMode() {
        val next = if (currentLanguage() == "bn") "en" else "bn"

        prefs.edit()
            .putString("language_mode", next)
            .putString("language_mode_updated_at", nowIso())
            .apply()

        refreshLanguageFoundation()

        val msg = if (next == "bn") "Bangla mode selected" else "English mode selected"
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun refreshLanguageFoundation() {
        val lang = currentLanguage()
        val updatedAt = prefs.getString("language_mode_updated_at", "never")

        languageStatusText.text = if (lang == "bn") {
            """
ভাষা মোড:
- বর্তমান: বাংলা
- আপডেট: $updatedAt
- নেটওয়ার্ক: বন্ধ
- সার্ভার sync: বন্ধ
- permission: নতুন কিছু নেই

বাংলা লেবেল preview:
- ডিভাইস পেয়ার করুন
- ফোন স্ট্যাটাস
- অ্যাপ খুলুন
- ভয়েস ড্রাফট
- সেফটি পলিসি
- ABU ওয়েব পোর্টাল

V10.8 policy:
এটা local-only language preference. কোনো dangerous permission নেই.
""".trimIndent()
        } else {
            """
Language Mode:
- Current: English
- Updated: $updatedAt
- Network: disabled
- Server sync: disabled
- Permission: no new permission

English label preview:
- Pair Device
- Phone Status
- Open Apps
- Voice Draft
- Safety Policy
- ABU Web Portal

V10.8 policy:
This is a local-only language preference. No dangerous permission.
""".trimIndent()
        }
    }

    private fun selectLauncherCard(name: String) {
        prefs.edit()
            .putString("last_launcher_card", name)
            .putString("last_launcher_card_at", nowIso())
            .apply()

        refreshLauncherShell()
        Toast.makeText(this, "Launcher card selected: $name", Toast.LENGTH_SHORT).show()
    }

    private fun openSafeUrl(label: String, url: String) {
        prefs.edit()
            .putString("last_launcher_card", label)
            .putString("last_launcher_card_at", nowIso())
            .putString("last_launcher_url", url)
            .apply()

        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            Toast.makeText(this, "Cannot open $label", Toast.LENGTH_SHORT).show()
        }

        refreshLauncherShell()
    }

    private fun refreshLauncherShell() {
        val lastCard = prefs.getString("last_launcher_card", "none")
        val lastAt = prefs.getString("last_launcher_card_at", "never")
        val lastUrl = prefs.getString("last_launcher_url", "none")

        launcherStatusText.text = """
ABU Launcher Shell:
- Mode: in-app launcher foundation
- Default Android launcher: not replaced
- HOME intent category: not added
- Background control: disabled
- Server execution: disabled
- Last card: $lastCard
- Last action time: $lastAt
- Last URL: $lastUrl

Quick shell cards:
1. ABU Web Portal
2. Pairing Portal
3. GOVO Express
4. Telegram
5. Phone Status
6. Voice Draft
7. Safety Policy

V9.9 policy:
This is only a launcher-style home inside ABU Native Helper.
It cannot replace the phone launcher yet.
""".trimIndent()
    }

    private fun refreshPhoneStatus() {
        val localId = getOrCreateLocalId()
        val pairingState = prefs.getString("pairing_state", "not_paired")
        val pairingCode = prefs.getString("pairing_code", "")
        val requestedAt = prefs.getString("pairing_requested_at", "never")

        val full = """
Local Identity:
- ABU Phone ID: $localId
- Pairing state: $pairingState
- Pairing code: ${if (pairingCode.isNullOrBlank()) "not set" else pairingCode}
- Pairing requested: $requestedAt

${getDeviceStatus()}

${getBatteryStatus()}

${getStorageStatus()}

Safety:
- Server sync: disabled
- Execution token: not issued
- Dangerous permissions: not requested
- Last refresh: ${nowIso()}
""".trimIndent()

        phoneStatusText.text = full
    }

    private fun refreshPairingStatus() {
        val localId = getOrCreateLocalId()
        val pairingCode = prefs.getString("pairing_code", "")
        val pairingState = prefs.getString("pairing_state", "not_paired")
        val requestedAt = prefs.getString("pairing_requested_at", "never")

        localIdText.text = localId

        statusText.text = """
Pairing state: $pairingState
Pairing code: ${if (pairingCode.isNullOrBlank()) "not set" else pairingCode}
Requested at: $requestedAt

Security:
- Real token: not issued
- Server sync: disabled
- Execution: disabled
- Dangerous permissions: not requested
""".trimIndent()
    }

    private fun refreshAll() {
        refreshPairingStatus()
        refreshPhoneStatus()
        refreshAppStatus()
        refreshNotificationFoundation()
        refreshVoiceFoundation()
    }

    private fun savePairingRequest() {
        val raw = pairInput.text.toString().trim().uppercase(Locale.US)

        if (raw.length < 4) {
            Toast.makeText(this, "Pairing code too short", Toast.LENGTH_SHORT).show()
            return
        }

        if (raw.length > 32) {
            Toast.makeText(this, "Pairing code too long", Toast.LENGTH_SHORT).show()
            return
        }

        val safe = Regex("^[A-Z0-9-]+$")
        if (!safe.matches(raw)) {
            Toast.makeText(this, "Use A-Z, 0-9 or dash only", Toast.LENGTH_SHORT).show()
            return
        }

        prefs.edit()
            .putString("pairing_code", raw)
            .putString("pairing_state", "pairing_request_saved_local_only")
            .putString("pairing_requested_at", nowIso())
            .apply()

        refreshAll()
        Toast.makeText(this, "Pairing request saved locally", Toast.LENGTH_SHORT).show()
    }

    private fun clearPairing() {
        prefs.edit()
            .remove("pairing_code")
            .putString("pairing_state", "not_paired")
            .putString("pairing_requested_at", "never")
            .apply()

        pairInput.setText("")
        refreshAll()
        Toast.makeText(this, "Pairing cleared", Toast.LENGTH_SHORT).show()
    }

    private fun addAppButton(root: LinearLayout, app: AllowedApp) {
        val installed = isAppInstalled(app.packageName)
        val button = Button(this).apply {
            text = if (installed) "Open ${app.label}" else "${app.label} not installed"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(if (installed) green else gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { openAllowedApp(app) }
        }
        root.addView(button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("abu_native_helper", Context.MODE_PRIVATE)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 34, 18, 28)
            setBackgroundColor(bg)
        }

        root.addView(tv("ABU Native Helper", 30f, textColor, true))
        root.addView(tv("V11.0 Pairing API Boundary", 15f, gold, true))
        root.addView(tv("Real pairing API contract foundation. No network call yet.", 15f, muted))


        root.addView(card(
            "🌐 Language Mode / ভাষা",
            "Switch English/Bangla label mode locally. No server sync, no permission."
        ))

        val languageToggleButton = Button(this).apply {
            text = "Toggle English / বাংলা"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener {
                toggleLanguageMode()
                refreshAll()
            }
        }
        root.addView(languageToggleButton)

        languageStatusText = tv("Loading language mode...", 14f, textColor)
        root.addView(languageStatusText)

        root.addView(card(
            "📱 Local ABU Phone ID",
            "Generated inside this app and stored locally. This is not IMEI and not a secret token."
        ))

        localIdText = tv("Loading...", 20f, green, true).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
        root.addView(localIdText)

        root.addView(card(
            "🔐 Pair Device",
            "Open https://ai.ecoluup.com/private/pairing/ and enter the pairing code here. V9.6 still saves it locally only."
        ))

        pairInput = EditText(this).apply {
            hint = "Enter pairing code"
            textSize = 18f
            setSingleLine(true)
            setTextColor(textColor)
            setHintTextColor(muted)
            setPadding(24, 18, 24, 18)
        }
        root.addView(pairInput)

        val saveButton = Button(this).apply {
            text = "Save Pairing Request"
            textSize = 17f
            setTextColor(Color.BLACK)
            background = rounded(green, 22f)
            setPadding(16, 16, 16, 16)
            setOnClickListener { savePairingRequest() }
        }
        root.addView(saveButton)

        val clearButton = Button(this).apply {
            text = "Clear Pairing"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 16, 16, 16)
            setOnClickListener { clearPairing() }
        }
        root.addView(clearButton)


        root.addView(card(
            "🔗 Pairing Portal Sync Design",
            "Prepare a local pairing sync draft. No server request and no token issue yet."
        ))

        val pairingSyncButton = Button(this).apply {
            text = "Prepare Pairing Sync Draft"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(green, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener {
                preparePairingSyncDraft()
                refreshAll()
            }
        }
        root.addView(pairingSyncButton)

        val openPairingSyncPortalButton = Button(this).apply {
            text = "Open Pairing Portal"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { openSafeUrl("Pairing Portal", "https://ai.ecoluup.com/private/pairing/") }
        }
        root.addView(openPairingSyncPortalButton)

        val clearPairingSyncButton = Button(this).apply {
            text = "Clear Pairing Sync Draft"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener {
                clearPairingSyncDraft()
                refreshAll()
            }
        }
        root.addView(clearPairingSyncButton)

        pairingSyncStatusText = tv("Loading pairing sync design...", 14f, textColor)
        root.addView(pairingSyncStatusText)


        root.addView(card(
            "🔐 Pairing API Boundary",
            "Prepare a local API request draft for future Web Node pairing. No server call yet."
        ))

        val prepareApiDraftButton = Button(this).apply {
            text = "Prepare Pairing API JSON"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(green, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener {
                preparePairingApiDraft()
                refreshAll()
            }
        }
        root.addView(prepareApiDraftButton)

        val shareApiDraftButton = Button(this).apply {
            text = "Share Pairing API Draft"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { sharePairingApiDraft() }
        }
        root.addView(shareApiDraftButton)

        val clearApiDraftButton = Button(this).apply {
            text = "Clear Pairing API Draft"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener {
                clearPairingApiDraft()
                refreshAll()
            }
        }
        root.addView(clearApiDraftButton)

        pairingApiStatusText = tv("Loading pairing API boundary...", 14f, textColor)
        root.addView(pairingApiStatusText)

        root.addView(card(
            "📊 Phone Status",
            "Local device, battery and storage status. No server sync yet."
        ))

        val refreshButton = Button(this).apply {
            text = "Refresh Phone Status"
            textSize = 17f
            setTextColor(Color.BLACK)
            background = rounded(green, 22f)
            setPadding(16, 16, 16, 16)
            setOnClickListener { refreshAll() }
        }
        root.addView(refreshButton)

        phoneStatusText = tv("Loading phone status...", 14f, textColor)
        root.addView(phoneStatusText)

        root.addView(card(
            "🚀 App Open Allowlist",
            "Only these local buttons can request app open. This is not remote execution."
        ))

        for (app in allowedApps) {
            addAppButton(root, app)
        }

        val settingsButton = Button(this).apply {
            text = "Open Android Settings"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { openAndroidSettings() }
        }
        root.addView(settingsButton)

        appStatusText = tv("Loading app allowlist...", 14f, textColor)
        root.addView(appStatusText)

        root.addView(card(
            "🔔 Notification Listener Foundation",
            "V9.6 keeps notification service disabled. It cannot read notifications yet."
        ))

        val notificationSettingsButton = Button(this).apply {
            text = "Open Notification Access Settings"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener {
                openNotificationAccessSettings()
                refreshAll()
            }
        }
        root.addView(notificationSettingsButton)

        notificationStatusText = tv("Loading notification foundation...", 14f, textColor)
        root.addView(notificationStatusText)

        root.addView(card(
            "🎤 Voice Command Foundation",
            "Write a command draft, save locally, then route it to Telegram manually. No microphone permission and no auto-send."
        ))

        voiceInput = EditText(this).apply {
            hint = "Example: /home or battery dekhao"
            textSize = 18f
            minLines = 2
            setTextColor(textColor)
            setHintTextColor(muted)
            setPadding(24, 18, 24, 18)
        }
        root.addView(voiceInput)

        val saveVoiceButton = Button(this).apply {
            text = "Save Voice Draft"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(green, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { saveVoiceDraft() }
        }
        root.addView(saveVoiceButton)

        val routeTelegramButton = Button(this).apply {
            text = "Route Draft to Telegram"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { routeDraftToTelegram() }
        }
        root.addView(routeTelegramButton)

        val clearVoiceButton = Button(this).apply {
            text = "Clear Voice Draft"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { clearVoiceDraft() }
        }
        root.addView(clearVoiceButton)

        voiceStatusText = tv("Loading voice foundation...", 14f, textColor)
        root.addView(voiceStatusText)


        root.addView(card(
            "👁️ Screen Vision Foundation",
            "V9.7 adds screenshot/OCR/UI-detection policy only. No screenshot capture is active."
        ))

        val screenVisionSettingsButton = Button(this).apply {
            text = "Open Display Settings"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener {
                openDisplaySettingsForScreenVision()
                refreshAll()
            }
        }
        root.addView(screenVisionSettingsButton)

        screenVisionStatusText = tv("Loading screen vision foundation...", 14f, textColor)
        root.addView(screenVisionStatusText)


        root.addView(card(
            "♿ Accessibility Service Prototype",
            "V9.8 adds a disabled AccessibilityService skeleton only. No tap, scroll, type, or screen read is active."
        ))

        val accessibilitySettingsButton = Button(this).apply {
            text = "Open Accessibility Settings"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener {
                openAccessibilitySettingsForAbu()
                refreshAll()
            }
        }
        root.addView(accessibilitySettingsButton)

        accessibilityStatusText = tv("Loading accessibility foundation...", 14f, textColor)
        root.addView(accessibilityStatusText)


        root.addView(card(
            "🏠 ABU Launcher Shell",
            "V9.9 adds an in-app command home. It does not replace Android launcher yet."
        ))

        val launcherHomeButton = Button(this).apply {
            text = "Select ABU Home Card"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(green, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { selectLauncherCard("ABU Home") }
        }
        root.addView(launcherHomeButton)

        val openWebPortalButton = Button(this).apply {
            text = "Open ABU Web Portal"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { openSafeUrl("ABU Web Portal", "https://ai.ecoluup.com") }
        }
        root.addView(openWebPortalButton)

        val openPairingPortalButton = Button(this).apply {
            text = "Open Pairing Portal"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { openSafeUrl("Pairing Portal", "https://ai.ecoluup.com/private/pairing/") }
        }
        root.addView(openPairingPortalButton)

        val openGovoButton = Button(this).apply {
            text = "Open GOVO Express"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(gold, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener { openSafeUrl("GOVO Express", "https://govoexpress.com") }
        }
        root.addView(openGovoButton)

        val openTelegramButton = Button(this).apply {
            text = "Open Telegram"
            textSize = 16f
            setTextColor(Color.BLACK)
            background = rounded(green, 22f)
            setPadding(16, 14, 16, 14)
            setOnClickListener {
                openAllowedApp(AllowedApp("Telegram", "org.telegram.messenger"))
                selectLauncherCard("Telegram")
            }
        }
        root.addView(openTelegramButton)

        launcherStatusText = tv("Loading launcher shell...", 14f, textColor)
        root.addView(launcherStatusText)

        root.addView(card(
            "🛡️ Safety Policy",
            "No root. No factory reset. No hidden screenshot. No silent spying. No microphone permission. No auto-send. No screenshot capture. No hidden screen read. No accessibility tap/scroll/type. No Android launcher replacement. No dangerous execution without audit + confirmation."
        ))

        statusText = tv("Loading pairing status...", 14f, muted)
        root.addView(statusText)

        root.addView(card(
            "🚀 Next",
            "V11.1 will create private Web Node pairing API skeleton."
        ))

        val scroll = ScrollView(this)
        scroll.addView(root)
        setContentView(scroll)

        refreshAll()
    }
}
