package com.abuos.nativehelper.core

object AbuBetaArchitecture {
    const val APP_VERSION = "1.3.0-v13.0"
    const val SPRINT_NAME = "V12 Full Beta Sprint"

    val phoneApkLayers = listOf(
        "Beta shell",
        "Local phone identity",
        "Language mode",
        "Phone status snapshot",
        "Pairing draft",
        "Pairing API contract draft",
        "Local audit model",
        "Safe command inbox model",
        "Web portal shortcuts"
    )

    val webNodeLayers = listOf(
        "Pairing API skeleton",
        "Device registry",
        "Audit event store",
        "Confirm / deny boundary",
        "Limited device token boundary",
        "Safe status reporting only"
    )

    fun sprintSummary(): String {
        return buildString {
            appendLine("$SPRINT_NAME — $APP_VERSION")
            appendLine("Phone APK layers:")
            phoneApkLayers.forEach { appendLine("- $it") }
            appendLine("Web Node layers:")
            webNodeLayers.forEach { appendLine("- $it") }
        }
    }
}
