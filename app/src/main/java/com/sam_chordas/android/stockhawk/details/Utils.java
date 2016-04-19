package com.sam_chordas.android.stockhawk.details;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by generaluser on 4/13/16.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static String convertUnixTimeStampToTimeStamp(String unixTimeStamp) {
        long timeStamp = Long.parseLong(unixTimeStamp) * 1000L;
        //Date date = new Date(timeStamp);
        return String.valueOf(timeStamp);
    }

    public static String getTimeOfDay(long timeStampInstant, long timeStampDay) {

        return null;
    }

    public static String getCompanyName(String jsonp) {
        JSONObject rootObj;
        JSONObject meta = null;
        // jsonp = padded json
        String json = jsonp.substring(jsonp.indexOf("(") + 1, jsonp.lastIndexOf(")"));
        try {
            rootObj = new JSONObject(json);
            meta = rootObj.getJSONObject("meta");
        } catch (JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
        }
        return meta.optString("Company-Name");
    }

    public static String convertToAmPmTime(String time) {
        String returnValue;
        String[] segment = time.split(":");
        String hour = segment[0];
        String min = segment[1];
        if (Integer.parseInt(hour) < 12) {
            returnValue = time + " AM";
        } else if (Integer.parseInt(hour) == 12) {
            returnValue = time + " PM";
        } else {
            returnValue = (Integer.parseInt(hour) - 12) + ":" + min + " PM";
        }
        return returnValue;
    }


    public static class InstantComparator implements Comparator<Instant> {
        @Override
        public int compare(Instant x, Instant y) {
            return x.compareTo(y);
        }
    }

    public static ArrayList<StockPriceHistory> parsePaddedJsonStringIntoStockPriceHistory(String jsonp) {
        JSONObject rootObj;
        ArrayList<StockPriceHistory> arrayList = new ArrayList<>();
        // jsonp is defined as XXX(json_string) where XXX is a callback function name, or just
        // padding of the json string.
        // grab the json contents in the jsonp string.
        String json = jsonp.substring(jsonp.indexOf("(") + 1, jsonp.lastIndexOf(")"));
        try {
            rootObj = new JSONObject(json);
            // get an instance of the jsonArray withing the JSON String
            JSONArray seriesArray = rootObj.getJSONArray("series");
            // iterate over the array
            for (int i = 0; i < seriesArray.length(); i++) {
                JSONObject obj = seriesArray.getJSONObject(i);
                String timeStamp = getTimeStamp(obj.optString("Timestamp"), obj.optString("Date"));
                String close = obj.optString("close");
                String high = obj.optString("high");
                String low = obj.optString("low");
                String open = obj.optString("open");
                String volume = obj.optString("volume");

                arrayList.add(new StockPriceHistory(
                        timeStamp,
                        close,
                        open,
                        high,
                        low,
                        volume));
            }
        } catch (JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
        }
        return arrayList;
    }

    private static String getTimeStamp(String unixTimeStamp, String inDate) {
        if (unixTimeStamp.length() > 0) {
            return convertUnixTimeStampToTimeStamp(unixTimeStamp);
        } else {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("YYYYMMdd");
            Instant instant = DateTime.parse(inDate, dtf).toInstant();
            return String.valueOf(instant.getMillis());
        }
    }

}
