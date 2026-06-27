package com.abuos.nativehelper.core

object AbuManualPostBridge {
    const val BRIDGE_VERSION = "v12.5"
    const val PRIVATE_ENDPOINT = "https://ai.ecoluup.com/private/api/device/pair/request"

    fun summary(): String {
        return buildString {
            appendLine("ABU Manual POST Bridge $BRIDGE_VERSION")
            appendLine("Endpoint: $PRIVATE_ENDPOINT")
            appendLine("Mode: manual curl/share only")
            appendLine("APK server call: disabled")
            appendLine("Credential storage: disabled")
            appendLine("Token issue: disabled")
            appendLine("Execution: disabled")
        }
    }
}
