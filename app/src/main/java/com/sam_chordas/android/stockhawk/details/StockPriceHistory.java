package com.sam_chordas.android.stockhawk.details;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by generaluser on 4/9/16.
 * represents the historical price of a stock
 */
public class StockPriceHistory implements Parcelable {

    // constants
    private final static String KEY_TIME_STAMP = "time_stamp";
    private final static String KEY_CLOSE_PRICE = "close_price" ;
    private final static String KEY_OPEN_PRICE = "open_price";
    private final static String KEY_HIGH_PRICE = "high_price";
    private final static String KEY_LOW_PRICE = "low_price";
    private final static String KEY_VOLUME = "volume";

    // members
    String mTimeStamp;
    String mClose;
    String mOpen;
    String mHigh;
    String mLow;
    String mVolume;

    public StockPriceHistory() {}

    public StockPriceHistory(String timeStamp, String close, String open, String high, String low, String volume) {
        mTimeStamp = timeStamp;
        mClose = close;
        mOpen = open;
        mHigh = high;
        mLow = low;
        mVolume = volume;
    }

    public StockPriceHistory(Parcel in ){
        if (in != null) {
            Bundle bundle = in.readBundle();
            this.mTimeStamp = bundle.getString(KEY_TIME_STAMP);
            this.mClose = bundle.getString(KEY_CLOSE_PRICE);
            this.mOpen = bundle.getString(KEY_OPEN_PRICE);
            this.mHigh = bundle.getString(KEY_HIGH_PRICE);
            this.mLow = bundle.getString(KEY_LOW_PRICE);
            this.mVolume = bundle.getString(KEY_VOLUME);
        }

    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        mTimeStamp = timeStamp;
    }

    public String getClose() {
        return mClose;
    }

    public void setClose(String close) {
        mClose = close;
    }

    public String getOpen() {
        return mOpen;
    }

    public String getDateTime() {

        long lng = Long.parseLong(getTimeStamp());

        if (isInstantToday(lng)) {
            LocalTime localTime = new LocalTime(lng);
            return DateTimeFormat.shortTime().print(localTime);
        } else {
            LocalDate localDate = new LocalDate(lng);
            return DateTimeFormat.shortDate().print(localDate);
        }
    }

    public void setOpen(String open) {
        mOpen = open;
    }

    public String getHigh() {
        return mHigh;
    }

    public void setHigh(String high) {
        mHigh = high;
    }

    public String getLow() {
        return mLow;
    }

    public void setLow(String low) {
        mLow = low;
    }

    public String getVolume() {
        return mVolume;
    }

    public void setVolume(String volume) {
        mVolume = volume;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TIME_STAMP, getTimeStamp());
        bundle.putString(KEY_CLOSE_PRICE, getClose());
        bundle.putString(KEY_OPEN_PRICE, getOpen());
        bundle.putString(KEY_HIGH_PRICE, getHigh());
        bundle.putString(KEY_LOW_PRICE, getLow());
        bundle.putString(KEY_VOLUME, getVolume());
        dest.writeBundle(bundle);
    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StockPriceHistory createFromParcel(Parcel in) {
            return new StockPriceHistory(in);
        }

        public StockPriceHistory[] newArray(int size) {
            return new StockPriceHistory[size];
        }
    };


    private static boolean isInstantToday(long l) {
        LocalDate someDateTime = new LocalDate(l);
        Instant startOfDate =  someDateTime.toDateTimeAtStartOfDay().toInstant();

        LocalDate now = new LocalDate(new Instant().toDateTime());
        Instant today = now.toDateTimeAtStartOfDay().toInstant();

        if (startOfDate.getMillis() == today.getMillis()) {
            return true;
        } else {
            return false;
        }
    }
}
