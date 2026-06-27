package com.abuos.nativehelper.core

enum class AbuAuditSeverity {
    INFO,
    REVIEW,
    BLOCKED
}

data class AbuAuditEvent(
    val eventId: String,
    val eventType: String,
    val severity: AbuAuditSeverity,
    val title: String,
    val detail: String,
    val createdAtIso: String
)

object AbuAuditTypes {
    const val APP_OPENED = "app_opened"
    const val PHONE_STATUS_REFRESHED = "phone_status_refreshed"
    const val PAIRING_DRAFT_CREATED = "pairing_draft_created"
    const val API_DRAFT_SHARED_MANUALLY = "api_draft_shared_manually"
    const val SAFETY_BLOCKED = "safety_blocked"
    const val COMMAND_UI_PREVIEWED = "command_ui_previewed"
}
