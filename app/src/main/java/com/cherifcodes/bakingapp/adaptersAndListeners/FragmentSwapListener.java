package com.cherifcodes.bakingapp.adaptersAndListeners;

import com.cherifcodes.bakingapp.model.Ingredient;

import java.util.List;

public interface FragmentSwapListener {
    void onFragmentSwapped(String fragmentName);

    void onSaveIngredients();

    List<Ingredient> getCurrIngredientList();
}
