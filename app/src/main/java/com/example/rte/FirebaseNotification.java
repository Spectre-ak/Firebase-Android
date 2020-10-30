package com.example.rte;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseNotification extends FirebaseMessagingService {
    NotificationManager mNotificationManager;
    NotificationChannel mChannel; Random random;
    Notification.Builder notification;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String id = "121212";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        mChannel = new NotificationChannel(id, "ChatApp",importance);
        mChannel.enableLights(true);
        mNotificationManager.createNotificationChannel(mChannel);
        notification = new Notification.Builder(getApplicationContext() , id).setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher_foreground1).setDefaults(Notification.DEFAULT_SOUND).setDefaults(Notification.DEFAULT_VIBRATE);
        mNotificationManager.notify(121212,notification.setContentTitle("Dummy Notification For All Users").build());
        super.onMessageReceived(remoteMessage);
    }
}
