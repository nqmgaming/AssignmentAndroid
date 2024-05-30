package com.nqmgaming.assignment_minhnqph31902.permission;

import android.content.BroadcastReceiver;

import androidx.core.app.NotificationManagerCompat;

public class CloseNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        // Close the notification
        int notificationId = intent.getIntExtra("notificationId", 0);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);
    }
}
