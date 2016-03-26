package com.example.francium.bm_notification;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by francium on 12.03.2016.
 */
public class Database extends SQLiteOpenHelper {

    private final static String DATABASE_NAME= "EgeBilMuh";

    private final static int DATABASE_VERSION = 1;

    private final static String TABLE_NAME= "EgeBilMuhNotific";
    private final static String NOTICE_ID= "NOTICE_ID";
    private final static String NOTICE_TITLE= "NOTICE_TITLE";
    private final static String NOTICE_HREF= "NOTICE_HREF";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = " CREATE TABLE "+TABLE_NAME+" ( "
                + NOTICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + NOTICE_TITLE + " TEXT , "
                + NOTICE_HREF + " TEXT "
                + " ) ";

        db.execSQL(CREATE_TABLE);
    }

    public void noticeDeleter(){   //cleantheTAble
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public String[] noticesearch(String titleOfNotifc){ //bu fonksiyon tıklanan listviewdeki itemin position değeri bulunur
        SQLiteDatabase db = this.getReadableDatabase();
        String[] ArrayNotific = new String[2];

        Cursor cursor = db.query(TABLE_NAME, new String[]{NOTICE_ID, NOTICE_TITLE, NOTICE_HREF},
                NOTICE_TITLE + " =? ", new String[]{titleOfNotifc}, null, null, null, null);
        if (cursor.moveToFirst())
        {
                ArrayNotific[0]=cursor.getString(1);
                ArrayNotific[1]=cursor.getString(2);
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
        return ArrayNotific;
    }


    public void noticeAdder(String argNoticeTitle,String argNoticeHref){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTICE_TITLE, argNoticeTitle );
        values.put(NOTICE_HREF,argNoticeHref);
        db.insert(TABLE_NAME, null, values);
        db.close();

    }
    public void noticeTableDropper(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public ArrayList<String> noticeGetter(){

        ArrayList<String> gotInfoAL = new ArrayList<String>();
        String selectQuery = "Select "+ NOTICE_TITLE +" FROM "+ TABLE_NAME; ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst())
        {
            do
            {
                gotInfoAL.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }

        return gotInfoAL;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

