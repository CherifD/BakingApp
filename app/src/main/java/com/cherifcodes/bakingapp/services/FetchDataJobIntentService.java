package com.cherifcodes.bakingapp.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.cherifcodes.bakingapp.IntentConstants;
import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.model.Repository;

import java.util.ArrayList;

import static com.cherifcodes.bakingapp.IntentConstants.UPDATE_WIDGET_LIST;

public class FetchDataJobIntentService extends JobIntentService {

    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 1010;
    private static ArrayList<Ingredient> mCurrIngredientList;
    private static int widgetId;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        mCurrIngredientList = new ArrayList<>();
        enqueueWork(context, FetchDataJobIntentService.class, JOB_ID, work);
    }

    public static ArrayList<Ingredient> getIngredientList() {
        return mCurrIngredientList;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        mCurrIngredientList = Repository.getInstance(FetchDataJobIntentService.this).getCurrIngredients();

        if (intent != null) {
            if (IntentConstants.UPDATE_WIDGET_LIST.equals(intent.getAction())) {
                // Get the widget id
                widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

                // Send an intent to open CollectionWidgetService
                Intent launchRemoteService = new Intent(getApplicationContext(), CollectionWidgetService.class);
                launchRemoteService.setAction(UPDATE_WIDGET_LIST);
                launchRemoteService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        widgetId);
                startService(launchRemoteService);

                updateWidget(widgetId);
            }
        } else {
            Log.i("FetIntentService", "Null intent!!!");
        }
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

        // Stop this service
        this.stopSelf();
    }
}
