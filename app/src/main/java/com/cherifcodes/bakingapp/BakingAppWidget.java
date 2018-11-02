package com.cherifcodes.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.cherifcodes.bakingapp.services.CollectionWidgetService;
import com.cherifcodes.bakingapp.services.FetchDataIntentService;

import static com.cherifcodes.bakingapp.IntentConstants.UPDATE_WIDGET_LIST;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i = 0; i < appWidgetIds.length; i++) {
            Log.i("BakingAppWidget", "onUpdate called inside for loop!");

            // Create an explicit intent to launch the FetchDataService
            //Intent fetchDataIntent = new Intent(context, FetchDataService.class);
            Intent fetchDataIntent = new Intent(context, FetchDataIntentService.class);
            fetchDataIntent.setAction(UPDATE_WIDGET_LIST);
            fetchDataIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            context.startService(fetchDataIntent);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * Creates and returns a new remoteView object for the widget
     */
    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        // Inflate the widget layout into a RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.baking_app_widget);

        // Create an intent to trigger the service that provides an adapter for the widget's ListView
        Intent adapterLoaderIntent = new Intent(context, CollectionWidgetService.class);
        // Pass the widget's id to the adapterLoaderIntent
        adapterLoaderIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        // Pass the adapter to the widget's listView
        remoteViews.setRemoteAdapter(R.id.appwidget_listView, adapterLoaderIntent);
        // Set an empty view in case of no data
        remoteViews.setEmptyView(R.id.appwidget_listView, R.id.appwidget_empty);

        // Create a PendingIntent to start the MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent activityStarterPendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.app_launcher, activityStarterPendingIntent);

        // Start the service that updates the adapter
        context.startService(adapterLoaderIntent);

        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(UPDATE_WIDGET_LIST)) {
            Log.i("BakingAppWidget", "Received an UPDATE_WIDGET_LIST broadcast!");
            // Get the widget's id from the received intent
            int appWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            // Get an instance of AppWidgetManager
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            // Get the widget's layout with associated data
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);

            // Create an PendingIntent to launch the FetchDataService and link it to the widget's
            // title clicks.
            Intent launchIntent = new Intent(context, FetchDataIntentService.class);
            launchIntent.setAction(UPDATE_WIDGET_LIST);
            launchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent launchMainPendingIntent = PendingIntent.getService(context, 0,
                    launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_title, launchMainPendingIntent);

            int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidget.class));
            // Have the AppWidgetManager update the widget
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_listView);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            super.onUpdate(context, appWidgetManager, widgetIds);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

