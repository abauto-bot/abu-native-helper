package com.abuos.nativehelper

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

/*
 * ABU OS V9.5 Notification Listener Foundation
 *
 * This service is intentionally disabled in AndroidManifest.xml.
 * It cannot run or read notifications in V9.5.
 *
 * Future phase will require:
 * - explicit user approval
 * - Android Notification Access permission
 * - ABU OS audit event
 * - confirmation policy
 * - no auto-reply without confirmation
 */
class AbuNotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        // V9.5: no operation
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        // V9.5: intentionally no read/log/send
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        // V9.5: intentionally no operation
    }
}
