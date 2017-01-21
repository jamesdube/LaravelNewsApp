package com.jamesdube.laravelnewsapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.util.NotificationManager;

import java.util.List;


/**
 * Created by rick on 1/15/17.
 /**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    //Refresh every 5 seconds
    public static final int SYNC_INTERVAL = 5;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    public static final String AUTHORITY    = "com.jamesdube.laravelnewsapp.provider";
    public static final String ACCOUNT_TYPE = "com.jamesdube.laravelnewsapp";
    public static final String ACCOUNT      = "Laravel Artisan";


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        System.out.println("xxxx constructor syncadapter....");
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        System.out.println("xxxx onPerformSync...");
        NotificationManager.newPostNotification("sync...");

    }

    private void saveData(List<Post> posts){
        System.out.println("xxxx inserted success. "+ String.valueOf(posts.size())+" inserted");
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = AUTHORITY;
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }*/
        ContentResolver.addPeriodicSync(account,authority,new Bundle(),90);
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name),ACCOUNT_TYPE);

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                AUTHORITY, bundle);
        Log.d("LNA", "syncImmediately: ");
        System.out.println("xxxx syncImmediately");
    }

    public static void initializeSyncAdapter(Context context) {
        System.out.println("xxxx initialize syncadapter....");
        getSyncAccount(context);
    }
}