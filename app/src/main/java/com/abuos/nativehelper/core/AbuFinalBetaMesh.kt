package com.abuos.nativehelper.core

object AbuFinalBetaMesh {
    const val VERSION = "1.3.0-v13.0"

    const val PRIVATE_PAIRING_DASHBOARD = "https://ai.ecoluup.com/private/api/device/pair/"
    const val PRIVATE_COMMAND_INBOX = "https://ai.ecoluup.com/private/api/device/pair/command/inbox"
    const val PRIVATE_COMMAND_DECISIONS = "https://ai.ecoluup.com/private/api/device/pair/command/decisions"

    val enabledBoundaries = listOf(
        "local_phone_identity",
        "pairing_request_draft",
        "manual_pairing_post_bridge",
        "confirm_deny_boundary",
        "safe_command_inbox",
        "command_approval_boundary"
    )

    val disabledCapabilities = listOf(
        "token_issue",
        "command_execution",
        "background_sync",
        "credential_storage_in_apk",
        "notification_access_enable",
        "accessibility_control_enable",
        "screen_capture",
        "microphone_recording",
        "default_launcher_replacement"
    )

    fun summary(): String {
        return buildString {
            appendLine("ABU Final Beta Mesh $VERSION")
            appendLine("Enabled boundaries:")
            enabledBoundaries.forEach { appendLine("- $it") }
            appendLine("Disabled capabilities:")
            disabledCapabilities.forEach { appendLine("- $it") }
        }
    }
}
