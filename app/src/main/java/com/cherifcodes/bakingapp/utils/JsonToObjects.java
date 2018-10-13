package com.cherifcodes.bakingapp.utils;

import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.model.Recipe;
import com.cherifcodes.bakingapp.model.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains static methods for creation of java objects from Json strings
 */
public class JsonToObjects {
    private static List<Recipe> recipeList;
    private static List<RecipeStep> recipeStepList;
    private static List<Ingredient> ingredientList;

    public static void processJsonStr(String jsonStr) {

        recipeList = new ArrayList<>();
        recipeStepList = new ArrayList<>();
        ingredientList = new ArrayList<>();

        try {
            JSONArray rootJsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < rootJsonArray.length(); i++) {
                // Get the recipe information
                JSONObject currJsonRecipe = rootJsonArray.getJSONObject(i);
                Recipe currRecipe = new Recipe(
                        currJsonRecipe.getInt("id") - 1,
                        currJsonRecipe.getInt("servings"),
                        currJsonRecipe.getString("name"),
                        currJsonRecipe.getString("image"));
                recipeList.add(currRecipe);

                // Get the ingredient information
                JSONArray jsonArrayOfIngredients = currJsonRecipe.getJSONArray("ingredients");
                for (int j = 0; j < jsonArrayOfIngredients.length(); j++) {
                    JSONObject currJsonIngredient = jsonArrayOfIngredients.getJSONObject(j);
                    Ingredient currIngredient = new Ingredient(
                            currRecipe.getRecipeId(),
                            currJsonIngredient.getInt("quantity"),
                            currJsonIngredient.getString("measure"),
                            currJsonIngredient.getString("ingredient"));
                    ingredientList.add(currIngredient);
                }

                // Get the recipe step information
                JSONArray jsonArrayOfSteps = currJsonRecipe.getJSONArray("steps");
                for (int k = 0; k < jsonArrayOfSteps.length(); k++) {
                    JSONObject currJsonRecipeStep = jsonArrayOfSteps.getJSONObject(k);
                    RecipeStep currRecipeStep = new RecipeStep(
                            currJsonRecipeStep.getInt("id"),
                            currRecipe.getRecipeId(),
                            currJsonRecipeStep.getString("shortDescription"),
                            currJsonRecipeStep.getString("description"),
                            currJsonRecipeStep.getString("videoURL"));
                    recipeStepList.add(currRecipeStep);
                }

            }
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    public static List<Recipe> getRecipeList() {
        return recipeList;
    }

    public static List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public static List<RecipeStep> getRecipeStepList() {
        return recipeStepList;
    }


}
