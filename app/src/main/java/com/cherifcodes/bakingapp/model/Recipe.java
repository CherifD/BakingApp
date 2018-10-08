package com.cherifcodes.bakingapp.model;

/**
 * Represents a single baking recipe fetched from a network location
 */
public class Recipe {

    private int recipeId;
    private int servingSize;
    private String recipeName;
    private String imageUrlStr;

    public Recipe(int recipeId, int servingSize, String recipeName, String imageUrlStr) {
        this.recipeId = recipeId;
        this.servingSize = servingSize;
        this.recipeName = recipeName;
        this.imageUrlStr = imageUrlStr;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getServingSize() {
        return servingSize;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getImageUrlStr() {
        return imageUrlStr;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", servingSize=" + servingSize +
                ", recipeName='" + recipeName + '\'' +
                '}';
    }
}
