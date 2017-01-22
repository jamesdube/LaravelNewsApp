package com.jamesdube.laravelnewsapp.util;

import android.app.Notification;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.models.Post;

import java.util.List;

/**
 * Created by rick on 1/17/17.
 */

public class Notify {

    public static void buildNotification(){

    }

    public static void newPostNotification(String content){
        NotificationCompat.Builder builder = getBuilder(content);

        Notification notification = builder.build();
        NotificationManagerCompat.from(App.getAppContext()).notify(0,notification);
        android.app.NotificationManager manager = (android.app.NotificationManager) App.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }

    @NonNull
    private static NotificationCompat.Builder getBuilder(String content) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getAppContext());
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentTitle("Laravel News")
                .setContentText(content);
        return builder;
    }

    public static void newPostNotification(List<Post> posts){
        int count = posts.size();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getAppContext());
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentTitle("Laravel News")
                .setContentText("("+ String.valueOf(count) +") new Post(s)...");

        Notification notification = builder.build();
        NotificationManagerCompat.from(App.getAppContext()).notify(0,notification);
        android.app.NotificationManager manager = (android.app.NotificationManager) App.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());

    }
}
