package com.example.francium.bm_notification;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by orkun on 27.03.2016.
 */
public class GCMListener extends GcmListenerService{

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
    }
}
