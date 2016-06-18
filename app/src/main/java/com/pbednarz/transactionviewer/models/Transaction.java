package com.pbednarz.transactionviewer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

import lombok.Value;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Value
public class Transaction implements Parcelable {
    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    private final BigDecimal amount;
    private final String sku;
    private final String currency;

    protected Transaction(Parcel in) {
        this.amount = (BigDecimal) in.readSerializable();
        this.sku = in.readString();
        this.currency = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.amount);
        dest.writeString(this.sku);
        dest.writeString(this.currency);
    }
}
