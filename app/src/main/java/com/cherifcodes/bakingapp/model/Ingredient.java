package com.cherifcodes.bakingapp.model;

import android.arch.persistence.room.Ignore;

/**
 * Represents a single ingredient for associated with the user's favorite recipe
 */
public class Ingredient {

    private int id;
    private int recipeId;
    private int quantity;
    private String measureUnit;
    private String ingredientName;

    public Ingredient(int id, int recipeId, int quantity, String measureUnit,
                      String ingredientName) {
        this.id = id;
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.ingredientName = ingredientName;
    }

    @Ignore
    public Ingredient(int recipeId, int quantity, String measureUnit, String ingredientName) {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measureUnit = measureUnit;
        this.ingredientName = ingredientName;
    }

    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "recipeId=" + recipeId +
                ", quantity=" + quantity +
                ", measureUnit='" + measureUnit + '\'' +
                ", ingredientName='" + ingredientName + '\'' +
                '}';
    }
}