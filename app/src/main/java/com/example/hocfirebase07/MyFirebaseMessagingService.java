package com.example.hocfirebase07;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }

        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        String channel_id = "wep_app_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.sms_2)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(uri)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCutomDesign(title, message));
        } else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.sms);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(channel_id,"web_app",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri,null);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0,builder.build());
    }

    private RemoteViews getCutomDesign(String title, String message) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.item);
        remoteViews.setTextViewText(R.id.txtTitle, title);
        remoteViews.setTextViewText(R.id.txtMessages, message);
        remoteViews.setImageViewResource(R.id.imgIcon, R.drawable.sms);
        return remoteViews;
    }
}
