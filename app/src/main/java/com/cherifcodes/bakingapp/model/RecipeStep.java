package com.cherifcodes.bakingapp.model;

/**
 * Represents a single step associated with a baking recipe
 */
public class RecipeStep {

    private int id;
    private int recipeId;
    private String shortDescription;
    private String description;
    private String videoUrlStr;

    public RecipeStep(int id, int recipeId, String shortDescription, String description,
                      String videoUrlStr) {
        this.id = id;
        this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrlStr = videoUrlStr;
    }

    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrlStr() {
        return videoUrlStr;
    }
}
