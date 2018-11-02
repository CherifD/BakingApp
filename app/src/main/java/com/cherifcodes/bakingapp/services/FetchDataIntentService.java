package com.cherifcodes.bakingapp.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cherifcodes.bakingapp.IntentConstants;
import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.model.Repository;

import java.util.ArrayList;

import static com.cherifcodes.bakingapp.IntentConstants.UPDATE_WIDGET_LIST;

public class FetchDataIntentService extends IntentService {

    private static ArrayList<Ingredient> mCurrIngredientList;
    private static int widgetId;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * name Used to name the worker thread, important only for debugging.
     */
    public FetchDataIntentService() {
        super("FetchDataIntentService");
        mCurrIngredientList = new ArrayList<>();
        //mCurrIngredientList = Repository.getInstance(FetchDataIntentService.this).getCurrIngredients();
    }

    public static ArrayList<Ingredient> getIngredientList() {
        return mCurrIngredientList;
    }

    public static int getAppWidgetId() {
        return widgetId;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mCurrIngredientList = Repository.getInstance(FetchDataIntentService.this).getCurrIngredients();
        Log.i("FetchIntentService", "List_size is: " + mCurrIngredientList.size());
        if (intent != null) {
            Log.i("FetchIntentService2", "Not null intent!!!");
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

        //this.stopSelf();
    }
}
