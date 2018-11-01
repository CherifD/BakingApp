package com.cherifcodes.bakingapp.services;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.model.Repository;

import java.util.ArrayList;

import static com.cherifcodes.bakingapp.IntentConstants.UPDATE_WIDGET_LIST;

public class FetchDataService extends Service {

    private static ArrayList<Ingredient> currIngredients;
    private static int mAppWidgetId;

    public static int getAppWidgetId() {
        return mAppWidgetId;
    }

    public static ArrayList<Ingredient> getIngredientList() {
        return currIngredients;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        currIngredients = new ArrayList<>();
        currIngredients = (ArrayList<Ingredient>) Repository.getInstance(getApplicationContext()).getAllIngredients();

        // Wait for the Repository's background thread to finish, so that the ListView Adapter class
        // won't get an empty List<Ingredient>. The problem is that the main thread does not wait
        // for the repository's thread to retrieve the list from the Room db. This solution is not ideal!
        while (currIngredients.size() <= 0) {
            currIngredients = (ArrayList<Ingredient>) Repository.getInstance(getApplicationContext()).getAllIngredients();
        }

        Log.i("FetchDataService", "onCreate called. List size = " + currIngredients.size());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get the widget's id from the received intent and launch the WidgetViewService
        if (intent != null && intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);


            // Send an intent to open CollectionWidgetService
            Intent launchRemoteService = new Intent(getApplicationContext(), CollectionWidgetService.class);
            launchRemoteService.setAction(UPDATE_WIDGET_LIST);
            launchRemoteService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    mAppWidgetId);
            startService(launchRemoteService);

            // Send a broadcast to update the widget
            this.updateWidget(mAppWidgetId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Sends a broadcast to the WidgetProvider using the UPDATE_WIDGET_LIST action
     */
    private void updateWidget(int appWidgetId) {
        // Create the intent to broadcast
        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(UPDATE_WIDGET_LIST);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        // Broadcast the intent
        sendBroadcast(widgetUpdateIntent);

        this.stopSelf();
    }
}
