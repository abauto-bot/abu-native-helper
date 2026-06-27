package com.abuos.nativehelper.core

object AbuSafetyPolicy {
    const val POLICY_VERSION = "v12.5"

    val blockedCapabilities = listOf(
        "root_execution",
        "hidden_screen_capture",
        "microphone_recording",
        "active_accessibility_control",
        "notification_listener_enable",
        "device_admin",
        "default_launcher_replacement",
        "silent_install",
        "factory_reset",
        "server_execution_token_inside_apk"
    )

    val allowedCapabilities = listOf(
        "local_phone_id",
        "manual_pairing_draft",
        "phone_status_snapshot",
        "local_language_preference",
        "manual_share_intent",
        "local_audit_preview",
        "safe_command_inbox_ui_only",
        "web_portal_shortcuts"
    )

    fun isBlocked(capability: String): Boolean {
        return blockedCapabilities.any { it.equals(capability, ignoreCase = true) }
    }

    fun summary(): String {
        return buildString {
            appendLine("ABU Safety Policy $POLICY_VERSION")
            appendLine("Allowed:")
            allowedCapabilities.forEach { appendLine("- $it") }
            appendLine("Blocked:")
            blockedCapabilities.forEach { appendLine("- $it") }
        }
    }
}
