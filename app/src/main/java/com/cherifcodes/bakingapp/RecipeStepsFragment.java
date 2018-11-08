package com.cherifcodes.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cherifcodes.bakingapp.adaptersAndListeners.FragmentSwapListener;
import com.cherifcodes.bakingapp.adaptersAndListeners.RecipeStepAdapter;
import com.cherifcodes.bakingapp.adaptersAndListeners.StepClickListener;
import com.cherifcodes.bakingapp.model.RecipeStep;
import com.cherifcodes.bakingapp.utils.JsonToObjects;
import com.cherifcodes.bakingapp.utils.ListProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Fragment} subclass for recipe steps.
 */
public class RecipeStepsFragment extends Fragment implements StepClickListener {
    private Button mViewIngredientsBtn;
    private List<RecipeStep> mRecipeStepList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecipeStepAdapter mRecipeStepAdapter;
    private FragmentSwapListener fragmentSwapListener;
    private StepClickListener mStepClickListener;

    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentLayoutView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        mRecyclerView = fragmentLayoutView.findViewById(R.id.rclv_recipe_steps);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Retrieve recipeId and recipeName from the parent activity
        Bundle bundle = this.getActivity().getIntent().getExtras();
        if (bundle == null) { // Ensure the bundle is not null
            Log.e(RecipeStepsFragment.class.getSimpleName(), "Null bundle!");
            return null;
        }
        int recipeId = bundle.getInt(IntentConstants.RECIPE_ID_KEY);
        this.getActivity().setTitle(bundle.getString(IntentConstants.RECIPE_NAME_KEY));

        // Get the current list of ingredients
        mRecipeStepList = ListProcessor.getRecipeStepsById(JsonToObjects.getRecipeStepList(),
                recipeId);
        // Initialize the Recipe list adapter and link it to the RecyclerView
        mRecipeStepAdapter = new RecipeStepAdapter(mRecipeStepList, this);
        mRecyclerView.setAdapter(mRecipeStepAdapter);

        // Initialize the FragmentSwapListener and link it to button clicks
        fragmentSwapListener = (FragmentSwapListener) getActivity();
        mViewIngredientsBtn = fragmentLayoutView.findViewById(R.id.btn_view_ingredients);
        mViewIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentSwapListener.onFragmentSwapped(RecipeStepsActivity.INGREDIENTS_FRAGMENT);
            }
        });
        // Initialize the RecipeClickListener
        mStepClickListener = (StepClickListener) getActivity();
        return fragmentLayoutView;
    }

    @Override
    public void onStepClicked(int recipeStepId) {
        if (!mStepClickListener.isTablet()) { // It's a phone, so launch the VideoPlayerActivity
            Intent intent = new Intent(this.getActivity(), VideoPlayerActivity.class);
            intent.putExtra(IntentConstants.VIDEO_URL_KEY,
                    mRecipeStepList.get(recipeStepId).getVideoUrlStr());
            intent.putExtra(IntentConstants.STEP_DESCRIPTION_KEY,
                    mRecipeStepList.get(recipeStepId).getDescription());
            intent.putExtra(IntentConstants.THUMBNAIL_IMAGE_URL_KEY,
                    mRecipeStepList.get(recipeStepId).getThumbnailImageUrlStr());
            startActivity(intent);
        } else { // It's a tablet, so have the RecipeStepActivity handle the click
            mStepClickListener.onStepClicked(recipeStepId);
        }
    }

    @Override
    public boolean isTablet() {
        return false;
    }
}
