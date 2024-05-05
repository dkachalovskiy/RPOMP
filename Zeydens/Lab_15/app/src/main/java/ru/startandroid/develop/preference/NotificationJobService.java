package ru.startandroid.develop.preference;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        // Ваш код для отправки уведомления
        String header = params.getExtras().getString("header");
        String content = params.getExtras().getString("content");
        int rowId = params.getExtras().getInt("rowId");
        // Создаем намерение для запуска активити с отображением уведомления
        Intent notificationIntent = new Intent(this, Notification.class);
        notificationIntent.putExtra("notificationTitle", header);
        notificationIntent.putExtra("notificationContent", content);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) rowId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Создаем канал уведомлений (для Android 8.0 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", channelName, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Создаем уведомление в шторке
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Тема: " + header)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground));

        // Отображаем уведомление в шторке
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) rowId, notificationBuilder.build());

        // Верните true, если задача требует завершения, или false, если задача будет продолжена в фоновом режиме
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // Верните true, чтобы повторно запустить задачу, если она не была выполнена
        return true;
    }
}
