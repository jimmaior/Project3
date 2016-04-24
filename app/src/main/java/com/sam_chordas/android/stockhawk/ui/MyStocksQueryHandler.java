package com.sam_chordas.android.stockhawk.ui;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by generaluser on 4/24/16.
 */
public class MyStocksQueryHandler extends AsyncQueryHandler {
    private MyStocksActivity mActivity;
    private String mSymbol;

    public MyStocksQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
        mActivity = (MyStocksActivity) cookie;
        mSymbol = selectionArgs[0];
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);

        mActivity.onStockSymbolLookup(mSymbol, cursor.moveToFirst());

    }
}
