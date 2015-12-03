package com.collegebaskettballstandings.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;



/**
 * Created by Scott on 11/23/2015.
 */
public class Notify {

    Context context;
    NotificationManager mNotificationManager;
    android.support.v4.app.NotificationCompat.Builder mBuilder;

    public Notify(Context context){
        this.context = context;
    }

    public void buildNotification(){

        mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("New Standings") // title for notification
                .setContentText("Standings Have Just Been Released! Check Them Out") // message for notification
                .setAutoCancel(true); // clear notification after click

        Intent intent = new Intent(context, RankingsActivity.class);
       PendingIntent pi = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_CANCEL_CURRENT);
       mBuilder.setContentIntent(pi);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(notification);
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void issueNotification(){
        mNotificationManager.notify(0, mBuilder.build());
    }
}
