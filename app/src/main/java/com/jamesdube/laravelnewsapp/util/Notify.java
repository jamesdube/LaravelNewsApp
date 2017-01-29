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
import android.util.Log;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.MainActivity;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.models.PostRepository;
import com.jamesdube.laravelnewsapp.posts.PostActivity;

import java.util.List;


public class Notify {
    
    protected static List<Post> notificationPosts;

    public static void showNewPostNotifications(){
        notificationPosts = PostRepository.getUnSeen();
        android.support.v4.app.NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();
            for(Post post : notificationPosts){
                inboxStyle.addLine(post.getTitle());
            }
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(App.getAppContext());
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setSound(getDefaultSoundUri())
                .setAutoCancel(true)
                .setContentTitle("Laravel News")
                .setContentText(getSummaryMessage())
                .setContentIntent(getNotificationIntent())
                .setStyle(inboxStyle)
                .setGroup("LNA_NOTIFICATIONS_GROUP");

        Notification notification = mBuilder.build();
        // Issue the group notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(App.getAppContext());
        notificationManager.notify(1, notification);
    }

    private static String getSummaryMessage(){

        if (notificationPosts.size() == 1){
            return notificationPosts.get(0).getTitle();
        }else{
            return String.valueOf(notificationPosts.size()) + " new Posts";
        }
    }

    private static PendingIntent getNotificationIntent(){
        Intent resultIntent;
        if (notificationPosts.size() == 1){

            Post post = notificationPosts.get(0);
            resultIntent = new Intent(App.getAppContext(), PostActivity.class);
            String json = post.toJson();
            resultIntent.putExtra("POST", json);
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
