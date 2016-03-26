package com.example.francium.bm_notification;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

/**
 * Created by francium on 12.03.2016.
 */
public class MyBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context argContx, Intent argINTNT){
        //uyarı geldiğinde ekrana toast mesajı basar,mesaja gelen uyarının aksiyonuna ekler
        Toast.makeText(argContx, "Isteğiniz başarıyla yerine getirildi..(Got info from ege bil muh)", Toast.LENGTH_SHORT).show();
        MainActivity ma = new MainActivity();
        ma.usingFetchInfo();
    }
}
