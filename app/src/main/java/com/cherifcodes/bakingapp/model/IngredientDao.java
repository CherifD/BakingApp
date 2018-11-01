package com.cherifcodes.bakingapp.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Ingredient> ingredientList);

    @Query("SELECT * FROM Ingredients ORDER BY recipeId DESC")
    List<Ingredient> getAllIngredients();

    @Query("DELETE FROM Ingredients")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM Ingredients")
    long getNumberOfRecords();

}
