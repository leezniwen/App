package com.example.msiqlab;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessageingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        ShowNotice(remoteMessage.getNotification().getTitle() , remoteMessage.getNotification().getBody());
    }
    public void ShowNotice(String title , String content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotice")
            .setContentTitle(title)
            .setSmallIcon(R.drawable.msiqlab_arc)
            .setAutoCancel(true)
            .setContentText(content);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999 , builder.build());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("Token" , token) ;

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }
}
