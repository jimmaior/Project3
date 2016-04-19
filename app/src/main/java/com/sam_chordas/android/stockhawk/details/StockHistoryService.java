package com.sam_chordas.android.stockhawk.details;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StockHistoryService extends IntentService {

    public static final int STATUS_RUNNING = 100;
    public static final int STATUS_ERROR = -100;
    public static final int STATUS_FINISHED = 200;

    public StockHistoryService() {
        super(TAG);
    }

    private static final String TAG = StockHistoryService.class.getSimpleName();

    String mStockHistory;
    private OkHttpClient mClient = new OkHttpClient();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onHandleIntent(Intent intent){
        Log.d(TAG, "onHandleIntent");
        final ResultReceiver receiver = intent.getParcelableExtra(Constants.RECEIVER);
        String command = intent.getStringExtra(Constants.COMMAND);
        Bundle bundle = new Bundle();
        if (command.equals(Constants.GET_STOCK_HISTORY)) {
            receiver.send(STATUS_RUNNING, null);
            try {
                String stockHistory = handleActionFetchStockDetails(intent);
                 if (stockHistory.length() > 0 ) {
                    bundle.putString("JSON_STRING", stockHistory);
                    //Log.d(TAG, bundle.getString("JSON_STRING"));
                    receiver.send(STATUS_FINISHED, bundle);
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, null);
            }
        }
    }

    @Nullable
    private String handleActionFetchStockDetails(Intent intent) {
        Log.d(TAG, "handleActionFetchStockDetails");

        StringBuilder urlStringBuilder = new StringBuilder();

        // Base URL for the Yahoo query
        // example: http://chartapi.finance.yahoo.com/instrument/1.0/MSFT/chartdata;type=quote;range=my/json
        urlStringBuilder.append("http://chartapi.finance.yahoo.com/instrument/1.0/");

        // get symbol from params.getExtra and build query
        String symbol = intent.getExtras().getString(Constants.TICKER_SYMBOL);
        String range = intent.getExtras().getString(Constants.RANGE);
        if (range.length() == 0) {
            range = "my";
        }
        try {
        // example: MSFT/chartdata;type=quote;range=my/json"
            urlStringBuilder.append(URLEncoder.encode(symbol, "UTF-8"));
            urlStringBuilder.append(URLEncoder.encode("//chartdata;type=quote;range=", "UTF-8"));
            urlStringBuilder.append(URLEncoder.encode(range + "//json", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String urlString;
        String getResponse;

        if (urlStringBuilder.toString().length() > 0){
            urlString = urlStringBuilder.toString();
            Log.d(TAG, urlString);
            try{
                getResponse = fetchData(urlString);
                mStockHistory = getResponse;
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return mStockHistory;
    }

    private String fetchData(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mClient.newCall(request).execute();
        String responseBody = response.body().string();
        if (responseBody.length() > 0) {
            return responseBody;
        } else {
            throw new IOException();
        }
    }
}
