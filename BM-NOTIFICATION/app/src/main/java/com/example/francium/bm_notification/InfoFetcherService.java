package com.example.francium.bm_notification;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by orkun on 27.03.2016.
 */
public class InfoFetcherService extends IntentService{

    private static final String AimedUrl = "http://bilmuh.ege.edu.tr/index.php";
    private Database db;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public InfoFetcherService(String name, Database db) {
        super(name);
        this.db = db;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ArrayList<String> gotInfoAL = new ArrayList<String>();
            Document doc  = Jsoup.connect(AimedUrl).get();
            Elements elements = doc.select("td.lsttd a");
            String anInfoString,anInfoHref;
            for(org.jsoup.nodes.Element anInfo:elements){
                anInfoString=anInfo.text();
                anInfoHref=anInfo.attr("href");
                gotInfoAL.add(anInfoString);
                db.noticeAdder(anInfoString,anInfoHref);       //gotInfoAL.add(anInfo.text());burda anInfoyu tekt ek ekliyecez
            }
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.example.francium.bm_notification");
            broadcastIntent.putExtra("gotInfoAL", gotInfoAL);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
