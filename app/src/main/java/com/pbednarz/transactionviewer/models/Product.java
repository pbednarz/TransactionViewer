package com.pbednarz.transactionviewer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Data
@AllArgsConstructor
public class Product implements Parcelable {
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    String sku;
    List<Transaction> transactions;

    protected Product(Parcel in) {
        this.sku = in.readString();
        this.transactions = in.createTypedArrayList(Transaction.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sku);
        dest.writeTypedList(this.transactions);
    }
}
