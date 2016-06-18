package com.pbednarz.transactionviewer.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Value;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Value
public class Transaction implements Parcelable {
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    private final String amount;
    private final String sku;
    private final String currency;

    protected Transaction(Parcel in) {
        this.amount = in.readString();
        this.sku = in.readString();
        this.currency = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.amount);
        dest.writeString(this.sku);
        dest.writeString(this.currency);
    }
}
