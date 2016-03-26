package com.example.francium.bm_notification;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Element;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by francium on 19.03.2016.
 */
public class notificdetails extends Activity {
    ProgressDialog progressDialog;
    private  String AimedUrl= "";
    private TextView tvTitle,tvHref,tvLink;
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificdetails);
        AimedUrl =getIntent().getExtras().getString("href");
        String title = getIntent().getExtras().getString("title");
        new FetchDetails().execute();
        tvTitle= (TextView)findViewById(R.id.tVtitle);
        tvHref= (TextView)findViewById(R.id.tVDetails);
        tvLink=(TextView)findViewById(R.id.tVLink);
        tvTitle.setText(title);
    }
    private void setTextInBackground(final String argDetails,final String html){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvHref.setText(argDetails);
                if(html!=""){
                    tvLink.setText("Want To Download?: "+html);
                    Linkify.addLinks(tvLink, Linkify.WEB_URLS);
                }
            }
        });
    }
    private String getMeFile(org.jsoup.nodes.Element anInfo){
        Elements qwer=anInfo.select("p a");
        String getMeFileLink = qwer.attr("href");
        getMeFileLink=getMeFileLink.replaceAll(" ","%20");
        return getMeFileLink;
    }

    public class FetchDetails extends AsyncTask<Void, Void, Void> {

        String title;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(notificdetails.this);
            progressDialog.setTitle("Getting..");
            progressDialog.setMessage("Get All The Info...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {


                Document doc  = Jsoup.connect(AimedUrl).get();
                Elements elements = doc.select("table > tbody > tr > td > table > tbody > tr > td:eq(0)");
                int i=0;
                String anInfoString="";
                String anInfoHref="";
                for(org.jsoup.nodes.Element anInfo:elements){
                    anInfoString=anInfo.text();
                    i++;
                    if(i==3)
                        anInfoHref=getMeFile(anInfo);
                }
                if (anInfoHref!=""){
                    anInfoHref="egeduyuru.ege.edu.tr"+anInfoHref;
                }
                setTextInBackground(anInfoString,anInfoHref);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog.dismiss();
        }

    }
}
