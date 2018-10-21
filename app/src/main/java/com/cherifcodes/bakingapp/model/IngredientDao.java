package com.cherifcodes.bakingapp.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredient(Ingredient ingredient);

    @Query("SELECT * FROM Ingredients WHERE recipeId = recipeId")
    LiveData<Ingredient> getIngredientByRecipeId(int recipeId);

}
