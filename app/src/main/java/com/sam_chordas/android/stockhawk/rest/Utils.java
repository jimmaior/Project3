package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.util.Log;

import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {

    private static String LOG_TAG = Utils.class.getSimpleName();

    public static boolean showPercent = true;

    public static ArrayList quoteJsonToContentVals(String JSON) {
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
        JSONObject jsonObject = null;
        JSONArray resultsArray = null;
        Log.i(LOG_TAG, "GET FB: " + JSON);
        try {
            jsonObject = new JSONObject(JSON);
            if (jsonObject != null && jsonObject.length() != 0) {
                jsonObject = jsonObject.getJSONObject("query");
                int count = Integer.parseInt(jsonObject.getString("count"));
                if (count == 1) {
                    jsonObject = jsonObject.getJSONObject("results")
                            .getJSONObject("quote");
                    try {
                        batchOperations.add(buildBatchOperation(jsonObject));
                    } catch (NumberFormatException nfe) {
                        throw nfe;
                    }
                } else {
                    resultsArray = jsonObject.getJSONObject("results").getJSONArray("quote");
                    if (resultsArray != null && resultsArray.length() != 0) {
                        for (int i = 0; i < resultsArray.length(); i++) {
                            jsonObject = resultsArray.getJSONObject(i);
                            try {
                                batchOperations.add(buildBatchOperation(jsonObject));
                            } catch (NumberFormatException nfe) {
                                throw nfe;
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "String to JSON failed: " + e);
        } catch (NumberFormatException nfe) {
            Log.e(LOG_TAG, "Format number failed:" + nfe.getMessage());
        }

        return batchOperations;
    }

    public static String truncateBidPrice(String bidPrice) {
        bidPrice = String.format("%.2f", Float.parseFloat(bidPrice));
        return bidPrice;
    }

    public static String truncateChange(String change, boolean isPercentChange) {
        String weight = change.substring(0, 1);
        String ampersand = "";
        if (isPercentChange) {
            ampersand = change.substring(change.length() - 1, change.length());
            change = change.substring(0, change.length() - 1);
        }
        change = change.substring(1, change.length());
        double round = (double) Math.round(Double.parseDouble(change) * 100) / 100;
        change = String.format("%.2f", round);
        StringBuffer changeBuffer = new StringBuffer(change);
        changeBuffer.insert(0, weight);
        changeBuffer.append(ampersand);
        change = changeBuffer.toString();
        return change;
    }

    public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject) {
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                QuoteProvider.Quotes.CONTENT_URI);
        try {
            String change = jsonObject.getString("Change");
            builder.withValue(QuoteColumns.SYMBOL, jsonObject.getString("symbol"));
            builder.withValue(QuoteColumns.BIDPRICE, truncateBidPrice(jsonObject.getString("Bid")));
            builder.withValue(QuoteColumns.PERCENT_CHANGE, truncateChange(jsonObject.getString("ChangeinPercent"), true));
            builder.withValue(QuoteColumns.CHANGE, truncateChange(change, false));
            builder.withValue(QuoteColumns.DAY_HIGH, jsonObject.getString("DaysHigh"));
            builder.withValue(QuoteColumns.DAY_LOW, jsonObject.getString("DaysLow"));
            builder.withValue(QuoteColumns.YEAR_HIGH, jsonObject.getString("YearHigh"));
            builder.withValue(QuoteColumns.YEAR_LOW, jsonObject.getString("YearLow"));
            builder.withValue(QuoteColumns.DAY_AVG_VOL, jsonObject.getString("AverageDailyVolume"));
            builder.withValue(QuoteColumns.VOLUME, jsonObject.getString("Volume"));
            builder.withValue(QuoteColumns.ISCURRENT, 1);
            if (change.charAt(0) == '-') {
                builder.withValue(QuoteColumns.ISUP, 0);
            } else {
                builder.withValue(QuoteColumns.ISUP, 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (java.lang.NumberFormatException nfe) {
            throw nfe;
        }
        return builder.build();
    }

}
