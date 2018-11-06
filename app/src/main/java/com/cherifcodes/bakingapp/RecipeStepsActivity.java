package com.cherifcodes.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cherifcodes.bakingapp.adaptersAndListeners.FragmentSwapListener;
import com.cherifcodes.bakingapp.adaptersAndListeners.StepClickListener;
import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.model.RecipeStep;
import com.cherifcodes.bakingapp.model.Repository;
import com.cherifcodes.bakingapp.utils.JsonToObjects;
import com.cherifcodes.bakingapp.utils.ListProcessor;

import java.util.List;

public class RecipeStepsActivity extends AppCompatActivity implements FragmentSwapListener,
        StepClickListener {
    public static final String INGREDIENTS_FRAGMENT = "recipe ingredients fragment";
    public static final String STEPS_FRAGMENT = "recipe steps fragment";
    public boolean isTablet;
    private FragmentManager mFragmentManager;
    private List<Ingredient> mCurrIngredientList;
    private List<RecipeStep> mCurrRecipeStepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Must initialize the next two statements before checking savedInstanceState for null
        setContentView(R.layout.activity_recipe_steps);
        mFragmentManager = getSupportFragmentManager();
        // Check if device is a tablet
        if (findViewById(R.id.vw_divider) != null)
            isTablet = true;

        int recipeId = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) recipeId = bundle.getInt(IntentConstants.RECIPE_ID_KEY);

        // Get the current lists of ingredients and recipe steps
        List<Ingredient> fullIngredientList = JsonToObjects.getIngredientList();
        List<RecipeStep> fullRecipeStepList = JsonToObjects.getRecipeStepList();
        mCurrIngredientList = ListProcessor.getIngredientsById(fullIngredientList, recipeId);
        mCurrRecipeStepList = ListProcessor.getRecipeStepsById(fullRecipeStepList, recipeId);

        if (savedInstanceState != null) return;
        // Check if the device is a tablet or phone before adding the fragments
        if (isTablet) {
            addTabletFragments();
        } else {
            addPhoneFragments();
        }
    }

    private void addTabletFragments() {
        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        // initialize the parameters for the VideoPlayerFragment
        String videoUrlString = mCurrRecipeStepList.get(0).getVideoUrlStr();
        String stepDescription = mCurrRecipeStepList.get(0).getDescription();
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.VIDEO_URL_KEY, videoUrlString);
        bundle.putString(IntentConstants.STEP_DESCRIPTION_KEY, stepDescription);
        videoPlayerFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container_steps, recipeStepsFragment, STEPS_FRAGMENT);
        fragmentTransaction.add(R.id.fragment_video_player_container_tab, videoPlayerFragment);
        fragmentTransaction.commit();
    }

    private void addPhoneFragments() {
        RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, recipeStepsFragment, STEPS_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentSwapped(String fragmentName) {
        if (INGREDIENTS_FRAGMENT.equals(fragmentName)) { // Ingredients list display is desired
            if (mFragmentManager != null) {
                if (isTablet) {
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_steps, new RecipeIngredientsFragment())
                            .addToBackStack(null)
                            .commit();
                } else { // Device is a phone
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new RecipeIngredientsFragment())
                            .addToBackStack(null)
                            .commit();
                }
            } else {
                Log.e(RecipeStepsActivity.class.getSimpleName(), "Null fragmentManager, ingredients.");
            }
        } else if (STEPS_FRAGMENT.equals(fragmentName)) { // Recipe step list display is desired
            if (mFragmentManager != null) {
                if (isTablet) {
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_steps, new RecipeStepsFragment())
                            .addToBackStack(null)
                            .commit();
                } else { // Device is a phone
                    mFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, new RecipeStepsFragment())
                            .addToBackStack(null)
                            .commit();
                }
            } else {
                Log.e(RecipeStepsActivity.class.getSimpleName(), "Null fragmentManager, recipes.");
            }
        }
    }

    /**
     * Saves the current list of Ingredients into the local Room database, but clears the
     * table before saving a new list
     */
    @Override
    public void onSaveIngredients() {
        // Insert the current list of Ingredients into the database
        Repository repository = Repository.getInstance(this);
        repository.deleteAll();
        repository.insertIngredients(mCurrIngredientList);
    }

    /**
     * This method is called by the RecipeIngredientsFragment to populate its list of Ingredients
     * @return the current list of Ingredients
     */
    @Override
    public List<Ingredient> getCurrIngredientList() {
        return mCurrIngredientList;
    }

    @Override
    public void onStepClicked(int recipeStepId) {
        // initialize the parameters for the VideoPlayerFragment
        String videoUrlString = mCurrRecipeStepList.get(recipeStepId).getVideoUrlStr();

        if (TextUtils.isEmpty(videoUrlString)) {
            Toast.makeText(this, R.string.no_video_err_msg, Toast.LENGTH_LONG).show();
        } else {
            VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
            // initialize the parameters for the VideoPlayerFragment
            String stepDescription = mCurrRecipeStepList.get(recipeStepId).getDescription();
            Bundle bundle = new Bundle();
            bundle.putString(IntentConstants.VIDEO_URL_KEY, videoUrlString);
            bundle.putString(IntentConstants.STEP_DESCRIPTION_KEY, stepDescription);
            videoPlayerFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_video_player_container_tab, videoPlayerFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean isTablet() {
        return isTablet;
    }
}
