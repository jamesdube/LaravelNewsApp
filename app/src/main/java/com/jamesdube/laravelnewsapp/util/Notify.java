package com.jamesdube.laravelnewsapp.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.MainActivity;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.models.Post;

import java.util.List;

/**
 * Created by rick on 1/17/17.
 */

public class Notify {
    
    protected static List<Post> notificationPosts;

    public static void buildNotification(){

    }

    public static void showNewPostNotifications(){
        notificationPosts = App.Realm().where(Post.class).findAll();
        android.support.v4.app.NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();
        for(Post post : notificationPosts)
        {
            inboxStyle.addLine(post.getTitle());
        }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(App.getAppContext());
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setSound(getDefaultSoundUri())
                .setAutoCancel(true)
                .setContentTitle("Laravel News")
                .setContentText(getNotificationMessage())
                .setContentIntent(getNotificationIntent())
                .setStyle(inboxStyle)
                .setGroup("LNA_NOTIFICATIONS_GROUP");

        Notification notification = mBuilder.build();
        // Issue the group notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(App.getAppContext());
        notificationManager.notify(1, notification);
    }

    @NonNull
    private static NotificationCompat.Builder getBuilder() {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getAppContext());
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentTitle("Laravel News")
                .setContentText(getNotificationMessage());
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

    private static String getNotificationMessage(){

        if (notificationPosts.size() <= 1){
            return notificationPosts.get(0).getTitle();
        }else{
            return String.valueOf(notificationPosts.size()) + " new Posts";
        }
    }

    private static PendingIntent getNotificationIntent(){
        Intent resultIntent;
        if (notificationPosts.size() <= 1){
            resultIntent = new Intent(App.getAppContext(), Post.class);
            resultIntent.putExtra("POST",App.Gson().toJson(notificationPosts.get(0),Post.class));
        }else{
            resultIntent = new Intent(App.getAppContext(), MainActivity.class);
        }
        return PendingIntent.getActivity(
                App.getAppContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }
    
    private static Uri getDefaultSoundUri() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }
}
