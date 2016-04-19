package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.sam_chordas.android.stockhawk.R;

/**
 * Created by generaluser on 4/18/16.
 */
public class QuoteWidgetProvider extends AppWidgetProvider {

    private static final String TAG = QuoteWidgetProvider.class.getSimpleName();

    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate:" + appWidgetIds.length);

        // update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; i++) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_collection);

            Intent intent = new Intent(context, QuoteWidgetRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv.setRemoteAdapter(R.id.widget_list, intent);

            // adding the collectionlist item handler
            Intent toastIntent = new Intent(context, QuoteWidgetRemoteViewsService.class);
            toastIntent.setAction(QuoteWidgetProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.widget_list, toastPendingIntent);

            // finally
            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled");
        super.onDisabled(context);
    }
}
