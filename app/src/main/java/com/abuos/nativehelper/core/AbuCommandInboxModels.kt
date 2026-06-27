package com.abuos.nativehelper.core

enum class AbuCommandRisk {
    SAFE_VIEW_ONLY,
    CONFIRM_REQUIRED,
    BLOCKED
}

enum class AbuCommandStatus {
    DRAFT,
    WAITING_CONFIRMATION,
    APPROVED_FOR_FUTURE,
    DENIED,
    BLOCKED
}

data class AbuCommandInboxItem(
    val commandId: String,
    val title: String,
    val body: String,
    val source: String,
    val risk: AbuCommandRisk,
    val status: AbuCommandStatus,
    val createdAtIso: String
)

object AbuCommandPolicy {
    fun classify(commandText: String): AbuCommandRisk {
        val lower = commandText.lowercase()

        val blockedWords = listOf(
            "factory reset",
            "wipe phone",
            "silent install",
            "record audio",
            "screen capture hidden",
            "enable accessibility",
            "device admin"
        )

        return if (blockedWords.any { lower.contains(it) }) {
            AbuCommandRisk.BLOCKED
        } else if (lower.contains("send") || lower.contains("open") || lower.contains("sync")) {
            AbuCommandRisk.CONFIRM_REQUIRED
        } else {
            AbuCommandRisk.SAFE_VIEW_ONLY
        }
    }
}
