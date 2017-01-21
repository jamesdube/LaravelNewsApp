package com.jamesdube.laravelnewsapp.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by rick on 1/15/17.
 /**
 * Define a Service that returns an IBinder for the
 * sync adapter class, allowing the sync adapter framework to call
 * onPerformSync().
 */
public class SyncService extends Service {
    // Storage for an instance of the sync adapter
    private static SyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        System.out.println("xxxx oncreate LNAsyncservice...");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }
    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     *
     */
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("xxxx onbind service....");
        return sSyncAdapter.getSyncAdapterBinder();
    }
}