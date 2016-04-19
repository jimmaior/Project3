package com.sam_chordas.android.stockhawk.details;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;


public class StockDetailActivity extends AppCompatActivity implements
        StockDetailFragment.OnChartButtonClickedListener,
        StockHistoryReceiver.Receiver {

    private static final String TAG = StockDetailActivity.class.getSimpleName();

    private static StockPrice mStockPrice;
    private static String mCompanyName;
    private static ArrayList<StockPriceHistory> mStockPriceHistoryList;
    private static String mSymbol;
    private static StockHistoryReceiver mStockHistoryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        Log.d(TAG, "onCreate():symbol=" + mSymbol);

        // retrieve the ticker symbol from the calling activity
        mSymbol = getIntent().getStringExtra(Constants.TICKER_SYMBOL);

        // create the fragment to display the data
        if (findViewById(R.id.fragment_container) != null) {
            // if restored from a previous state
            if (savedInstanceState != null) {
                return;
            }
        }

        // get the stock history for the symbol
        fetchStockPriceHistory("");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStockHistoryReceiver != null) {
            mStockHistoryReceiver.setReceiver(this);
        }
    }

    @Override
    public void onPause() {
        mStockHistoryReceiver.setReceiver(null);
        super.onPause();
    }

    public void onReceiveResult(int resultCode, Bundle data) {
        Log.d(TAG, "onReceiveResult resultCode:" + resultCode);
        switch (resultCode) {
            case StockHistoryService.STATUS_RUNNING:
                // progress bar
                break;
            case StockHistoryService.STATUS_FINISHED:
                String jsonp = data.getString("JSON_STRING");
                // Log.d(TAG, "JSONP = " + jsonp);
                mStockPriceHistoryList = new ArrayList<>();
                mStockPriceHistoryList = Utils.parsePaddedJsonStringIntoStockPriceHistory(jsonp);
                mCompanyName = Utils.getCompanyName(jsonp);
                createStockPriceDetailAndHistoryBundle();
                updateFragment();
                break;
            case StockHistoryService.STATUS_ERROR:
                Toast.makeText(this, "Error occurred while attempting to retrieve historical stock data", Toast.LENGTH_LONG)
                        .show();
                break;
        }
    }

    private void fetchStockPriceHistory(String range) {
        // start the service to interact with the Stock History API
        // set a reference to the API Request response handler
        mStockHistoryReceiver = new StockHistoryReceiver(new Handler());
        mStockHistoryReceiver.setReceiver(this);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, this, StockHistoryService.class);
        serviceIntent.putExtra(Constants.COMMAND, Constants.GET_STOCK_HISTORY);
        serviceIntent.putExtra(Constants.RECEIVER, mStockHistoryReceiver);
        serviceIntent.putExtra(Constants.TICKER_SYMBOL, mSymbol);
        serviceIntent.putExtra(Constants.RANGE, range);

        if (isConnected) {
            startService(serviceIntent);
        }
    }

    @Override
    public void onChartButtonClicked(String range) {
        Log.d(TAG, "range:" + range);
        fetchStockPriceHistory(range);
        updateFragment();

    }

    private void updateFragment() {

        Bundle args = new Bundle();
        args.putParcelable(Constants.STOCK_PRICE, mStockPrice);
        args.putString(Constants.COMPANY_NAME, mCompanyName);
        args.putParcelableArrayList(Constants.STOCK_PRICE_HISTORY, mStockPriceHistoryList);


        StockDetailFragment fragment = (StockDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (fragment != null ) {
            fragment.updateChartData(args);
        } else {
            // create new fragment
            fragment = new StockDetailFragment();
            fragment.setArguments(args);
            // add the fragment to the fragment_container FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

    private void createStockPriceDetailAndHistoryBundle(){

        // StockPrice
        // pull data for the current stock ticker
        String projection[] = new String[9];
        projection[0] = QuoteColumns.SYMBOL;
        projection[1] = QuoteColumns.BIDPRICE;
        projection[2] = QuoteColumns.CHANGE;
        projection[3] = QuoteColumns.DAY_HIGH;
        projection[4] = QuoteColumns.DAY_LOW;
        projection[5] = QuoteColumns.YEAR_HIGH;
        projection[6] = QuoteColumns.YEAR_LOW;
        projection[7] = QuoteColumns.DAY_AVG_VOL;
        projection[8] = QuoteColumns.VOLUME;

        Cursor cursor = getContentResolver().query(
                QuoteProvider.Quotes.CONTENT_URI,
                projection,
                QuoteColumns.SYMBOL + "= ? AND " + QuoteColumns.ISCURRENT + " = 1",
                new String[] { mSymbol},
                null);

         if (cursor != null ) {
            cursor.moveToFirst();
            String quote = cursor.getString(cursor.getColumnIndex(QuoteColumns.BIDPRICE));
            String change = cursor.getString(cursor.getColumnIndex(QuoteColumns.CHANGE));
            String dayHighPrice = cursor.getString(cursor.getColumnIndex(QuoteColumns.DAY_HIGH));
            String dayLowPrice = cursor.getString(cursor.getColumnIndex(QuoteColumns.DAY_LOW));
            String yearHighPrice = cursor.getString(cursor.getColumnIndex(QuoteColumns.YEAR_HIGH));
            String yearLowPrice = cursor.getString(cursor.getColumnIndex(QuoteColumns.YEAR_LOW));
            String dayAvgVolume = cursor.getString(cursor.getColumnIndex(QuoteColumns.DAY_AVG_VOL));
            String volume = cursor.getString(cursor.getColumnIndex(QuoteColumns.VOLUME));

            mStockPrice = new StockPrice(
                    mSymbol, "", "",
                    quote, change, yearHighPrice, yearLowPrice,
                    dayHighPrice, dayLowPrice, dayAvgVolume, volume);
        }

    }
}
