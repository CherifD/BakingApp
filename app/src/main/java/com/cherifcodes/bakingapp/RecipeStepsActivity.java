package com.cherifcodes.bakingapp;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cherifcodes.bakingapp.adaptersAndListeners.FragmentSwapListener;

public class RecipeStepsActivity extends AppCompatActivity implements FragmentSwapListener {
    public static final String INGREDIENTS_FRAGMENT = "recipe ingredients fragment";
    public static final String STEPS_FRAGMENT = "recipe steps fragment";

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Must initialize the next two statements before checking savedInstanceState for null
        setContentView(R.layout.activity_recipe_steps);
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) return;

        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, recipeStepsFragment, STEPS_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentSwapped(String fragmentName) {
        if (INGREDIENTS_FRAGMENT.equals(fragmentName)) {
            if (mFragmentManager != null) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new RecipeIngredientsFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Log.e(RecipeStepsActivity.class.getSimpleName(), "Null fragmentManager, ingredients.");
            }
        } else if (STEPS_FRAGMENT.equals(fragmentName)) {
            if (mFragmentManager != null) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new RecipeStepsFragment())
                        .addToBackStack(null)
                        .commit();
            } else {
                Log.e(RecipeStepsActivity.class.getSimpleName(), "Null fragmentManager, recipes.");
            }
        }

    }
}
