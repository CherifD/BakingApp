package com.cherifcodes.bakingapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cherifcodes.bakingapp.adaptersAndListeners.RecipeAdapter;
import com.cherifcodes.bakingapp.adaptersAndListeners.RecipeClickListener;
import com.cherifcodes.bakingapp.model.Recipe;
import com.cherifcodes.bakingapp.utils.JsonFetcher;
import com.cherifcodes.bakingapp.utils.JsonToObjects;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeClickListener {

    //private TextView displayJsonTextView;


    private RecyclerView mRecyclerView;

    private List<Recipe> mRecipeList = new ArrayList<>();
    private RecipeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rclv_recipes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //displayJsonTextView = findViewById(R.id.tv_display_json_result);
        new FetchRecipesAsync().execute(JsonFetcher.RECIPE_JSON_URL);
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

            mRecipeList = JsonToObjects.getRecipeList();
            mAdapter = new RecipeAdapter(MainActivity.this);
            mAdapter.setRecipeList(mRecipeList);
            mRecyclerView.setAdapter(mAdapter);
            /*List<Ingredient> ingredientList = JsonToObjects.getIngredientList();
            ingredientList = ListProcessor.getIngredientsById(ingredientList, mRecipeList.get(0)
                    .getRecipeId());
            List<RecipeStep> recipeStepList = JsonToObjects.getRecipeStepList();
            recipeStepList = ListProcessor.getRecipeStepsById(recipeStepList, mRecipeList.get(0)
                    .getRecipeId());*/

            /*displayJsonTextView.append("recipeList size: " + mRecipeList.size() + "\n");
            displayJsonTextView.append("ingredientList size: " + ingredientList.size() + "\n");
            displayJsonTextView.append("recipeStepList size: " + recipeStepList.size() + "\n\n");

            displayJsonTextView.append(mRecipeList.get(0).getRecipeName() + "\n");
            displayJsonTextView.append(mRecipeList.get(0).getRecipeId() + "\n\n");

            displayJsonTextView.append(ingredientList.get(0).getIngredientName() + "\n");
            displayJsonTextView.append(ingredientList.get(0).getRecipeId() + "\n");
            displayJsonTextView.append(ingredientList.get(0).getMeasureUnit() + "\n");
            displayJsonTextView.append(ingredientList.get(0).getQuantity() + "\n\n");

            displayJsonTextView.append(recipeStepList.get(0).getShortDescription() + "\n");
            displayJsonTextView.append(recipeStepList.get(0).getRecipeId() + "\n");
            displayJsonTextView.append(recipeStepList.get(0).getId() + "\n");
            displayJsonTextView.append(recipeStepList.get(0).getDescription() + "\n");
            displayJsonTextView.append(recipeStepList.get(0).getVideoUrlStr() + "\n");*/
        }
    }
}
