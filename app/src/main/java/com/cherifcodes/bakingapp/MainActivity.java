package com.cherifcodes.bakingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.model.Recipe;
import com.cherifcodes.bakingapp.model.RecipeStep;
import com.cherifcodes.bakingapp.utils.JsonFetcher;
import com.cherifcodes.bakingapp.utils.JsonToObjects;
import com.cherifcodes.bakingapp.utils.ListProcessor;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView displayJsonTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayJsonTextView = findViewById(R.id.tv_display_json_result);
        new FetchRecipesAsync().execute(JsonFetcher.RECIPE_JSON_URL);
    }


    private class FetchRecipesAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url = JsonFetcher.buildUrl(strings[0]);
            return JsonFetcher.getJsonResponse(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JsonToObjects.processJsonStr(s);

            List<Recipe> recipeList = JsonToObjects.getRecipeList();
            List<Ingredient> ingredientList = JsonToObjects.getIngredientList();
            ingredientList = ListProcessor.getIngredientsById(ingredientList, recipeList.get(0)
                    .getRecipeId());
            List<RecipeStep> recipeStepList = JsonToObjects.getRecipeStepList();
            recipeStepList = ListProcessor.getRecipeStepsById(recipeStepList, recipeList.get(0)
                    .getRecipeId());

            displayJsonTextView.append("recipeList size: " + recipeList.size() + "\n");
            displayJsonTextView.append("ingredientList size: " + ingredientList.size() + "\n");
            displayJsonTextView.append("recipeStepList size: " + recipeStepList.size() + "\n\n");

            displayJsonTextView.append(recipeList.get(0).getRecipeName() + "\n");
            displayJsonTextView.append(recipeList.get(0).getRecipeId() + "\n\n");

            displayJsonTextView.append(ingredientList.get(0).getIngredientName() + "\n");
            displayJsonTextView.append(ingredientList.get(0).getRecipeId() + "\n");
            displayJsonTextView.append(ingredientList.get(0).getMeasureUnit() + "\n");
            displayJsonTextView.append(ingredientList.get(0).getQuantity() + "\n\n");

            displayJsonTextView.append(recipeStepList.get(0).getShortDescription() + "\n");
            displayJsonTextView.append(recipeStepList.get(0).getRecipeId() + "\n");
            displayJsonTextView.append(recipeStepList.get(0).getId() + "\n");
            displayJsonTextView.append(recipeStepList.get(0).getDescription() + "\n");
            displayJsonTextView.append(recipeStepList.get(0).getVideoUrlStr() + "\n");
        }
    }
}
