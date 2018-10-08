package com.cherifcodes.bakingapp.utils;

import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.model.RecipeStep;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that performs list processing, such as filtering by id.
 */
public class ListProcessor {

    /**
     * Given a list of recipe steps, this method returns a sublist of recipes, which are associated
     * with a specified recipe id
     *
     * @param list     the original list of recipe steps to process
     * @param recipeId the specified recipe id
     * @return the filtered version of the recipe step list.
     */
    public static List<RecipeStep> getRecipeStepsById(List<RecipeStep> list, int recipeId) {
        // Validate list and recipeId
        if (list == null || list.size() <= 0 || recipeId < 0) {
            return null;
        }
        ArrayList<RecipeStep> filteredList = new ArrayList<>();
        for (RecipeStep recipeStep : list) {
            if (recipeStep.getRecipeId() == recipeId) {
                filteredList.add(recipeStep);
            }
        }
        return filteredList;
    }

    /**
     * Given a list of ingredients, this method returns a sublist of ingredients, which are associated
     * with a specified recipe id
     *
     * @param list     the original list of ingredients to process
     * @param recipeId the specified recipe id
     * @return the filtered version of the ingredient list.
     */
    public static List<Ingredient> getIngredientsById(List<Ingredient> list, int recipeId) {
        // Validate list and recipeId
        if (list == null || list.size() <= 0 || recipeId < 0) {
            return null;
        }

        ArrayList<Ingredient> filteredList = new ArrayList<>();
        for (Ingredient ingredient : list) {
            if (ingredient.getRecipeId() == recipeId) {
                filteredList.add(ingredient);
            }
        }
        return filteredList;
    }
}
