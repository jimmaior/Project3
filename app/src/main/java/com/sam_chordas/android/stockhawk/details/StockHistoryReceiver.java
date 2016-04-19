package com.sam_chordas.android.stockhawk.details;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

/**
 * Created by generaluser on 4/4/16.
 */
@SuppressLint("ParcelCreator")
public class StockHistoryReceiver extends ResultReceiver {

    private static final String TAG = StockHistoryReceiver.class.getSimpleName();

    private Receiver mReceiver;

    // Interface for communication
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    // constructor takes a handler
    public StockHistoryReceiver(Handler handler) {
        super(handler);
        Log.d(TAG, "StockHistoryReceiver");
    }

    // setter for assigning the receiver
    public void setReceiver(Receiver receiver) {
        this.mReceiver = receiver;
    }

    // delegate method which passes the result to the receiver if the receiver had been assigned
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}