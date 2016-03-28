package com.example.francium.bm_notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GcmListenerService;


/**
 * Created by orkun on 27.03.2016.
 */
public class GCMListener extends GcmListenerService{

    private BroadcastReceiver BL;
    private long lastUpdate;

    public GCMListener() {
        super();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        lastUpdate = sharedPreferences.getLong("lastUpdate", 0);
        BL = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

    }

}
