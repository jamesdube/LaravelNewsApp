package com.jamesdube.laravelnewsapp.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.jamesdube.laravelnewsapp.App;
import com.jamesdube.laravelnewsapp.R;
import com.jamesdube.laravelnewsapp.http.Client;
import com.jamesdube.laravelnewsapp.http.requests.onGetPosts;
import com.jamesdube.laravelnewsapp.http.requests.onSavePosts;
import com.jamesdube.laravelnewsapp.models.Post;
import com.jamesdube.laravelnewsapp.models.PostRepository;
import com.jamesdube.laravelnewsapp.util.Notify;

import java.util.List;

import io.realm.Realm;


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
    public static final String SYNC_FINISHED      = "com.jamesdube.SYNC_FINISHED";
    public static final String SYNC_FINISHED_ERROR = "com.jamesdube.SYNC_FINISHED_ERROR";


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        System.out.println("xxxx constructor syncadapter....");
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        System.out.println("xxxx onPerformSync...");

        PostRepository.fetch(new onSavePosts() {
            @Override
            public void onSaved() {
                //Broadcast the sync result
                Intent i = new Intent(SYNC_FINISHED);
                sendBroadcast(i);
            }

            @Override
            public void onError() {
                //Broadcast the sync result
                Intent i = new Intent(SYNC_FINISHED_ERROR);
                sendBroadcast(i);
            }
        });
    }

    private void sendBroadcast(Intent i) {
        App.getAppContext().sendBroadcast(i);
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = AUTHORITY;
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
        //syncImmediately(context);
    }

    public static void syncImmediately(Context context) {

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                AUTHORITY, bundle);
        System.out.println("xxxx syncImmediately");
    }

    public static void initializeSyncAdapter(Context context) {
        System.out.println("xxxx initialize syncadapter....");
        getSyncAccount(context);
    }
}