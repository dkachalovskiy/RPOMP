package com.example.lab15;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;
import static androidx.core.app.NotificationCompat.PRIORITY_LOW;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private final String ChannelID = "my_channel_01";
    private final String ChannelName = "my_channel_01";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (!nm.areNotificationsEnabled()) return;

        String id = intent.getStringExtra("_id");
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");

        Intent resultIntent = new Intent(context, NotificationViewActivity.class);
        resultIntent.putExtra("_id", id);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(ChannelID,
                    ChannelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(notificationChannel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ChannelID);

            builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setContentIntent(resultPendingIntent);
            nm.notify(1, builder.build());
        } else {
            Notification notification = new NotificationCompat.Builder(context, ChannelID)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(resultPendingIntent)
                    .build();

            NotificationManager notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.notify(1, notification);
            }
        }


    }
}
