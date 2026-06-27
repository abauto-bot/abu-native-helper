package com.abuos.nativehelper

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/*
 * ABU OS V9.8 Accessibility Service Prototype
 *
 * This service is intentionally disabled in AndroidManifest.xml.
 *
 * V9.8 cannot:
 * - tap
 * - scroll
 * - type
 * - read screen content
 * - perform gestures
 * - execute commands
 *
 * Future activation requires:
 * - explicit user approval
 * - Android Accessibility permission
 * - ABU OS audit event
 * - risk classification
 * - confirmation phrase
 * - visible emergency stop
 */
class AbuAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        // V9.8: no operation
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // V9.8: intentionally no read/log/send/action
    }

    override fun onInterrupt() {
        // V9.8: intentionally no operation
    }
}
