package com.abuos.nativehelper.core

private fun jsonSafe(value: String): String {
    return value
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
}

data class DeviceIdentity(
    val localPhoneId: String,
    val appPackage: String = "com.abuos.nativehelper",
    val appVersion: String = AbuBetaArchitecture.APP_VERSION,
    val languageMode: String = "en",
    val createdAtIso: String
)

data class PhoneStatusSnapshot(
    val manufacturer: String,
    val model: String,
    val androidRelease: String,
    val sdkInt: Int,
    val batteryPercent: Int?,
    val storageSummary: String,
    val capturedAtIso: String
)

data class PairingRequestDraft(
    val apiVersion: String = "v13.0",
    val mode: String = "pairing_request_draft_only",
    val endpointFuture: String = "https://ai.ecoluup.com/api/device/pair/request",
    val localPhoneId: String,
    val pairingCode: String,
    val appVersion: String = AbuBetaArchitecture.APP_VERSION,
    val languageMode: String,
    val requestedAtIso: String
) {
    fun toStableJson(): String {
        return """
{
  "api_version": "${jsonSafe(apiVersion)}",
  "mode": "${jsonSafe(mode)}",
  "endpoint_future": "${jsonSafe(endpointFuture)}",
  "phone_node": {
    "local_phone_id": "${jsonSafe(localPhoneId)}",
    "pairing_code": "${jsonSafe(pairingCode)}",
    "app_package": "com.abuos.nativehelper",
    "app_version": "${jsonSafe(appVersion)}",
    "language_mode": "${jsonSafe(languageMode)}"
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
  "requested_at": "${jsonSafe(requestedAtIso)}"
}
""".trimIndent()
    }
}
