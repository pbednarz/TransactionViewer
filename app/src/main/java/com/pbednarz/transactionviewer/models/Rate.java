package com.pbednarz.transactionviewer.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.RoundingMode;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by pbednarz on 2016-06-18.
 */
@Value
@AllArgsConstructor
public class Rate implements Parcelable {
    public static final Parcelable.Creator<Rate> CREATOR = new Parcelable.Creator<Rate>() {
        @Override
        public Rate createFromParcel(Parcel source) {
            return new Rate(source);
        }

        @Override
        public Rate[] newArray(int size) {
            return new Rate[size];
        }
    };
    private final String from;
    private final String rate;
    private final String to;

    protected Rate(Parcel in) {
        this.from = in.readString();
        this.rate = in.readString();
        this.to = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.from);
        dest.writeString(this.rate);
        dest.writeString(this.to);
    }

    public Rate invert() {
        return new Rate(to, BigDecimal.ONE.divide(new BigDecimal(rate), RoundingMode.HALF_UP).toPlainString(), from);
    }
}
