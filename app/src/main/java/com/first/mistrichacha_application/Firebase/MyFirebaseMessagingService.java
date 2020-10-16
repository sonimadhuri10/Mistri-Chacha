package com.first.mistrichacha_application.Firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.first.mistrichacha_application.Activity.DrawerActivity;
import com.first.mistrichacha_application.Comman.SessionManagment;
import com.first.mistrichacha_application.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private Intent intent;
    SessionManagment sd;
    int num;
    private NotificationChannel mChannel ; ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String s1  = remoteMessage.getNotification().getBody();
        String s2 = remoteMessage.getNotification().getTitle();
        String s3 = remoteMessage.getNotification().getIcon();

        //type = remoteMessage.getData().get("type");
        sd = new SessionManagment(this);

        Log.d("notificationDAATA", "From: " + s1);

      sendNotification(s1,s2,s3);  //message is Parameter


    }

    private void sendNotification(String body,String title, String icon) {

        String CHANNEL_ID = "Madhu";
        String CHANNEL_NAME = "Notification" ;

        num = (int) System.currentTimeMillis();

        intent = new Intent(this, DrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fragment", "notification");

        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, num, intent, PendingIntent.FLAG_ONE_SHOT );
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_HIGH ;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationCompat.Builder builder ;

                if(mChannel == null){
                    mChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,importance);
                    mChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(mChannel);
                }

                builder = new NotificationCompat.Builder(this , CHANNEL_ID);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                 pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT );
                 builder.setSmallIcon(R.drawable.launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

                notificationManager.notify(0, builder.build());

            }else{

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

                notificationManager.notify(0, notificationBuilder.build());
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



}