package com.cherifcodes.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

        return fragmentLayoutView;
    }

    @Override
    public void onStepClicked(int recipeStepId) {
        String stepVideoUrl = mRecipeStepList.get(recipeStepId).getVideoUrlStr();

        if (TextUtils.isEmpty(stepVideoUrl)) {
            Toast.makeText(this.getActivity(), R.string.no_video_err_msg, Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this.getActivity(), VideoPlayerActivity.class);
            intent.putExtra(IntentConstants.VIDEO_URL_KEY,
                    mRecipeStepList.get(recipeStepId).getVideoUrlStr());
            intent.putExtra(IntentConstants.STEP_DESCRIPTION_KEY,
                    mRecipeStepList.get(recipeStepId).getDescription());
            startActivity(intent);
        }
    }

}
