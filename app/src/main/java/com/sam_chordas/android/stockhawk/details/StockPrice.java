package com.sam_chordas.android.stockhawk.details;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by generaluser on 4/9/16.
 */
public class StockPrice implements Parcelable {

    private static final String TAG = StockPrice.class.getSimpleName();

    // constants
    private final static String KEY_SYMBOL = "symbol";
    private final static String KEY_COMPANY_NAME = "company_name";
    private final static String KEY_TIMESTAMP = "time_stamp";
    private final static String KEY_QUOTE = "quote";
    private final static String KEY_CHANGE = "change";
    private final static String KEY_YEAR_HIGH_PRICE = "year_high_price";
    private final static String KEY_YEAR_LOW_PRICE = "year_low_price";
    private final static String KEY_DAY_HIGH_PRICE = "day_high_price";
    private final static String KEY_DAY_LOW_PRICE = "day_low_price";
    private final static String KEY_AVG_DAY_VOLUME = "avg_day_volume";
    private final static String KEY_VOLUME = "volume";
    
    
    // members
    String mSymbol;
    String mCompanyName;
    String mTimeStamp;
    String mQuote;
    String mChange;
    String mYearHighQuote;
    String mYearLowQuote;
    String mDayHighQuote;
    String mDayLowQuote;
    String mAvgDayVolume;
    String mVolume;
    List<StockPriceHistory> mStockPriceHistory;

    // Constructors
    public StockPrice() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SYMBOL, getTimeStamp());
        bundle.putString(KEY_COMPANY_NAME, getCompanyName());
        bundle.putString(KEY_TIMESTAMP, getTimeStamp());
        bundle.putString(KEY_QUOTE, getQuote());
        bundle.putString(KEY_CHANGE, getChange());
        bundle.putString(KEY_YEAR_HIGH_PRICE, getYearHighQuote());
        bundle.putString(KEY_YEAR_LOW_PRICE, getYearLowQuote());
        bundle.putString(KEY_DAY_HIGH_PRICE, getDayHighQuote());
        bundle.putString(KEY_DAY_LOW_PRICE, getDayLowQuote());
        bundle.putString(KEY_AVG_DAY_VOLUME, getAvgDayVolume());
        bundle.putString(KEY_VOLUME, getVolume());
        dest.writeBundle(bundle);
    }

    public StockPrice(String symbol,
                      String companyName,
                      String timeStamp,
                      String quote,
                      String change,
                      String yearHighQuote,
                      String yearLowQuote,
                      String dayHighQuote,
                      String dayLowQuote,
                      String avgDayVolume,
                      String volume) {
        mSymbol = symbol;
        mCompanyName = companyName;
        mTimeStamp = timeStamp;
        mQuote = quote;
        mChange = change;
        mYearHighQuote = yearHighQuote;
        mYearLowQuote = yearLowQuote;
        mDayHighQuote = dayHighQuote;
        mDayLowQuote = dayLowQuote;
        mAvgDayVolume = avgDayVolume;
        mVolume = volume;
    }

    public StockPrice(Parcel in) {
        if (in != null) {
            Bundle b = in.readBundle();
            this.mSymbol = b.getString(KEY_SYMBOL);
            this.mCompanyName = b.getString(KEY_COMPANY_NAME);
            this.mTimeStamp = b.getString(KEY_TIMESTAMP);
            this.mQuote = b.getString(KEY_QUOTE);
            this.mChange = b.getString(KEY_CHANGE);
            this.mYearHighQuote = b.getString(KEY_YEAR_HIGH_PRICE);
            this.mYearLowQuote = b.getString(KEY_YEAR_LOW_PRICE);
            this.mDayHighQuote = b.getString(KEY_DAY_HIGH_PRICE);
            this.mDayLowQuote = b.getString(KEY_DAY_LOW_PRICE);
            this.mAvgDayVolume = b.getString(KEY_AVG_DAY_VOLUME);
            this.mVolume = b.getString(KEY_VOLUME);
        }
    }

    public String getSymbol() {
        return mSymbol;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public String getQuote() {
        return mQuote;
    }

    public String getChange() {
        return mChange;
    }

    public String getYearHighQuote() {
        return mYearHighQuote;
    }

    public String getYearLowQuote() {
        return mYearLowQuote;
    }

    public String getDayHighQuote() {
        return mDayHighQuote;
    }

    public String getDayLowQuote() {
        return mDayLowQuote;
    }

    public String getAvgDayVolume() {
        return mAvgDayVolume;
    }

    public String getVolume() {
        return mVolume;
    }

    public ArrayList<StockPriceHistory> getStockPriceHistory() {
        return (ArrayList) mStockPriceHistory;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StockPrice createFromParcel(Parcel in) {
            return new StockPrice(in);
        }

        public StockPrice[] newArray(int size) {
            return new StockPrice[size];
        }
    };

}
