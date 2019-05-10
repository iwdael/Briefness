package com.hacknife.demo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PaCs implements Parcelable {
    protected PaCs(Parcel in) {
    }

    public static final Creator<PaCs> CREATOR = new Creator<PaCs>() {
        @Override
        public PaCs createFromParcel(Parcel in) {
            return new PaCs(in);
        }

        @Override
        public PaCs[] newArray(int size) {
            return new PaCs[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
