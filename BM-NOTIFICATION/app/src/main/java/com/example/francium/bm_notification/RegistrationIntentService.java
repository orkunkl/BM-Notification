package com.example.francium.bm_notification;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by orkun on 27.03.2016.
 */
public class RegistrationIntentService extends IntentService{

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {

            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sharedPreferences.edit().putBoolean("sentToken", true).apply();
            Log.i(TAG, "GCM Registration Token: " + token);

        } catch (Exception e){

            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean("sentToken", false).apply();
        }
        Intent registrationComplete = new Intent("registrationComplete");
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
