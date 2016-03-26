package com.example.francium.bm_notification;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.DocumentsContract;
import android.renderscript.Element;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> gotInfoAL = new ArrayList<String>();
    private static final String AimedUrl = "http://bilmuh.ege.edu.tr/index.php";
    private ProgressDialog progressDialog;
    private Database db = new Database(this);

    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.listView1);
        //we wanted from there to print all the data from the database in mphone

        gotInfoAL=db.noticeGetter();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,gotInfoAL );

        lv.setAdapter(arrayAdapter);
        //Now, we wrote all titles in a listview

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                        String selectedNotific = lv.getItemAtPosition(position).toString();

                        String[] arrayNotific = db.noticesearch(selectedNotific);
                        Intent intent = new Intent(MainActivity.this, notificdetails.class);
                        intent.putExtra("title",arrayNotific[0]);
                        intent.putExtra("href",arrayNotific[1]);
                        startActivity(intent);

                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {  //Refresh e bastığında
            Context context = getApplicationContext();
            new FetchInfo().execute(); //toGetAll Info from bil muh ege

            return true;

        }
        if (id == R.id.action_alarm) {
            Context context = getApplicationContext();
            ısConnectedToInt();
            //setAlarm();
            return true;

        }
        if (id == R.id.action_alarmnondefault) {
            Context context = getApplicationContext();
            setAlarmDefault();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean ısConnectedToInt(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public void setAlarmDefault(){
        Context ctx=this;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        Intent in= new Intent(ctx, MyBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, in, 0);
        AlarmManager am = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);

        am.cancel(pi); // cancel any existing alarms
        if(ısConnectedToInt())
          new FetchInfo().execute();

        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                calendar.getTimeInMillis() + AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY, pi); //to create a scheduled plans 2nd 3rd parameters if u change delay the alarm in milisec

        /*am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY, pi);*/
    }
    public void setAlarm(){
        Context ctx=this;

        Intent in= new Intent(ctx, MyBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(ctx, 0, in, 0);
        AlarmManager am = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);

        am.cancel(pi); // cancel any existing alarms

        if(ısConnectedToInt())
            new FetchInfo().execute();

        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY, pi); //to create a scheduled plans 2nd 3rd parameters if u change delay the alarm in milisec

        /*am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY, pi);*/
    }
    public void usingFetchInfo(){
        new FetchInfo().execute(); //toGetAll Info from bil muh ege
    }
    public class FetchInfo extends AsyncTask<Void, Void, Void> {

        String title;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Getting..");
            progressDialog.setMessage("Get All The Info...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //class="lsttd"
            try {
                db.noticeDeleter();
                gotInfoAL.clear();

                Document doc  = Jsoup.connect(AimedUrl).get();
                Elements elements = doc.select("td.lsttd a");
                String anInfoString,anInfoHref;
                for(org.jsoup.nodes.Element anInfo:elements){
                    anInfoString=anInfo.text();
                    anInfoHref=anInfo.attr("href");
                    gotInfoAL.add(anInfoString);
                    db.noticeAdder(anInfoString,anInfoHref);       //gotInfoAL.add(anInfo.text());burda anInfoyu tekt ek ekliyecez
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //In there, we should write gotInfoAL on database
            ArrayAdapter<String> arrayAdapterEmpty = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,new ArrayList<String>());
            lv.setAdapter( arrayAdapterEmpty);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,gotInfoAL );
            lv.setAdapter(arrayAdapter);

            progressDialog.dismiss();
        }

    }

}