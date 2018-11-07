package com.cherifcodes.bakingapp.receivers;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.cherifcodes.bakingapp.MainActivity;
import com.cherifcodes.bakingapp.R;
import com.cherifcodes.bakingapp.services.CollectionWidgetService;

public class BakingAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Update all widgets on the list
        for (int i = 0; i < appWidgetIds.length; i++) {
            // Create a new RemoteViews object from the widget layout
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.baking_app_widget);

            //////////// Intent_1 to update the collection view
            // Create an explicit intent to the RemoteViewsService, which will call its
            // factory (or adapter) to provide collection data to the collection view
            Intent fetchDataIntent = new Intent(context, CollectionWidgetService.class);
            // Connect the intent to the widget's collection view
            remoteViews.setRemoteAdapter(R.id.appwidget_listView, fetchDataIntent);
            // Set an empty view in case of no data
            remoteViews.setEmptyView(R.id.appwidget_listView, R.id.appwidget_empty);

            //////////// Intent_2 to handle widget update when user taps widget's title
            // Create an update PendingIntent with the AppWidgetManager.ACTION_APPWIDGET_UPDATE action
            // This PendingIntent will trigger a call to this onUpdate method when the widget title
            // TextView is tapped.
            Intent intentUpdate = new Intent(context, BakingAppWidgetProvider.class);
            intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Update the current widget instance only, by creating an array that contains
            // the widget’s unique ID
            int[] idArray = new int[]{appWidgetIds[i]};
            intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
            //Wrap the intent as a PendingIntent, using PendingIntent.getBroadcast()
            PendingIntent updatePendingIntent = PendingIntent.getBroadcast(context, appWidgetIds[i],
                    intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
            // Send the pending intent in response to the user tapping the ‘Update’ TextView
            remoteViews.setOnClickPendingIntent(R.id.widget_title, updatePendingIntent);

            //////////// Intent_3 to launch the app when user taps the "start app" TextView
            // Set up a PendingIntent for launching the app from the widget
            Intent launcherIntent = new Intent(context, MainActivity.class);
            PendingIntent launcherPendingIntent = PendingIntent.getActivity(context, 0,
                    launcherIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.app_launcher, launcherPendingIntent);

            // Have the AppWidgetManager call the RemoteViewFactory (or adapter)'s onDataSetChanged()
            // methods and perform a widget update
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.appwidget_listView);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
