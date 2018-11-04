package com.cherifcodes.bakingapp.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.cherifcodes.bakingapp.adaptersAndListeners.IngredientListWidgetAdapter;


public class CollectionWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        // Instantiate the collection adapter with the loaded data
        return new IngredientListWidgetAdapter(getApplicationContext());
    }
}
