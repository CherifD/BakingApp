package com.cherifcodes.bakingapp.adaptersAndListeners;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.cherifcodes.bakingapp.R;
import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.services.FetchDataIntentService;

import java.util.ArrayList;

public class IngredientListWidgetAdapter implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Ingredient> mIngredientList;
    private Context mContext;

    public IngredientListWidgetAdapter(Context context) {
        mContext = context;
        mIngredientList = new ArrayList<>();
        //mIngredientList = FetchDataService.getIngredientList();
        mIngredientList = FetchDataIntentService.getIngredientList();

        if (mIngredientList != null)
            Log.i("ConsIngredientViewFact", "list size = " + mIngredientList.size());
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        if (mIngredientList != null)
            mIngredientList.clear();
    }

    @Override
    public int getCount() {
        return mIngredientList == null ? 0 : mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient currIngredient = mIngredientList.get(position);

        RemoteViews itemView = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);

        itemView.setTextViewText(R.id.widget_name_ingredient, currIngredient.getIngredientName());
        itemView.setTextViewText(R.id.widget_quantity_ingredient,
                String.valueOf(currIngredient.getQuantity()));
        itemView.setTextViewText(R.id.widget_unit_ingredient, currIngredient.getMeasureUnit());
        return itemView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
