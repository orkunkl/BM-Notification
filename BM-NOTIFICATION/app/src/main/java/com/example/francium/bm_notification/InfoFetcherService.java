package com.example.francium.bm_notification;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.francium.bm_notification.Utils.DatabaseElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by orkun on 27.03.2016.
 */
public class InfoFetcherService extends IntentService{

    private static final String AimedUrl = "http://bilmuh.ege.edu.tr/index.php";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public InfoFetcherService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ArrayList<String> gotInfoAL = new ArrayList<String>();
            ArrayList<DatabaseElement> databaseElements = new ArrayList<DatabaseElement>();
            Document doc  = Jsoup.connect(AimedUrl).get();
            Elements elements = doc.select("td.lsttd a");
            String anInfoString,anInfoHref;
            for(org.jsoup.nodes.Element anInfo:elements){
                anInfoString=anInfo.text();
                anInfoHref=anInfo.attr("href");
                gotInfoAL.add(anInfoString);

                databaseElements.add(new DatabaseElement(anInfoString, anInfoHref));
            }
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.example.francium.bm_notification.MainActivity");
            broadcastIntent.putExtra("var", "Fetch");
            broadcastIntent.putStringArrayListExtra("gotInfoAL", gotInfoAL);
            broadcastIntent.putParcelableArrayListExtra("databaseElements", databaseElements);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
