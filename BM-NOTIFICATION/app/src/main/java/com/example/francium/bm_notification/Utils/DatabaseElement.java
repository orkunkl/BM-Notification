package com.example.francium.bm_notification.Utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by orkun on 27.03.2016.
 */
public class DatabaseElement implements Parcelable{

    public String anInfoString;
    public String anInfoHref;

    public DatabaseElement(String str, String href){
        anInfoString = str;
        anInfoHref = href;
    }

    protected DatabaseElement(Parcel in) {
        anInfoString = in.readString();
        anInfoHref = in.readString();
    }

    public static final Creator<DatabaseElement> CREATOR = new Creator<DatabaseElement>() {
        @Override
        public DatabaseElement createFromParcel(Parcel in) {
            return new DatabaseElement(in);
        }

        @Override
        public DatabaseElement[] newArray(int size) {
            return new DatabaseElement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(anInfoString);
        dest.writeString(anInfoHref);
    }
}
