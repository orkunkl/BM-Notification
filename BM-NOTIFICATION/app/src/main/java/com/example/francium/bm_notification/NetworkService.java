package com.example.francium.bm_notification;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.security.Policy;

/**
 * Created by orkun on 27.03.2016.
 */
public class NetworkService extends Service {

    private BroadcastListener BL;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BL = new BroadcastListener();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     Broadcasts from Google Cloud Messaging or website changes
     **/
    private class BroadcastListener extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("var")=="GCM"){
             //GCM thing
            } else if(intent.getStringExtra("var")=="Fetch"){
                //Duyuru website thing
            }
        }
    }
}
