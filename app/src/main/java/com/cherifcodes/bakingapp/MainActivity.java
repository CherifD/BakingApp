package com.cherifcodes.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cherifcodes.bakingapp.adaptersAndListeners.RecipeAdapter;
import com.cherifcodes.bakingapp.adaptersAndListeners.RecipeClickListener;
import com.cherifcodes.bakingapp.model.Recipe;
import com.cherifcodes.bakingapp.utils.JsonFetcher;
import com.cherifcodes.bakingapp.utils.JsonToObjects;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeClickListener {

    // Number of columns for the recipe list when running on a tablet
    private static final int NUM_RECYCLER_VIEW_COLUMNS = 3;

    private RecyclerView mRecyclerView;
    private List<Recipe> mRecipeList = new ArrayList<>();
    private RecipeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use a grid layout for tablet and a linear layout for phone
        if (findViewById(R.id.rclv_recipes_tablet) != null) {
            mRecyclerView = findViewById(R.id.rclv_recipes_tablet);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                    NUM_RECYCLER_VIEW_COLUMNS);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            mRecyclerView = findViewById(R.id.rclv_recipes);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        if (this.isConnectedToTheInternet()) { // There is an internet connection
            new FetchRecipesAsync().execute(JsonFetcher.RECIPE_JSON_URL);
        } else { // There is no internet connection, show a toast message.
            Toast.makeText(this, R.string.no_internet_error_message,
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Determines if the device is connected to the internet
     *
     * @return true if there is a network connection, false otherwise
     */
    private boolean isConnectedToTheInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }

    @Override
    public void onRecipeClick(int recipeId) {
        Intent intent = new Intent(this, RecipeStepsActivity.class);
        intent.putExtra(IntentConstants.RECIPE_ID_KEY, recipeId);
        intent.putExtra(IntentConstants.RECIPE_NAME_KEY, mRecipeList.get(recipeId).getRecipeName());
        this.startActivity(intent);
    }

    private class FetchRecipesAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = JsonFetcher.buildUrl(strings[0]);
            return JsonFetcher.getJsonResponse(url);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) return;

            JsonToObjects.processJsonStr(s);

            // Get the list of recipes for the RecipeAdapter, initialize the adapter and pass the
            // adapter to the recyclerView
            mRecipeList = JsonToObjects.getRecipeList();
            mAdapter = new RecipeAdapter(MainActivity.this);
            mAdapter.setRecipeList(mRecipeList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
