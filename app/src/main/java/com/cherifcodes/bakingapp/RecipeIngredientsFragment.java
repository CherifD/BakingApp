package com.cherifcodes.bakingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cherifcodes.bakingapp.adaptersAndListeners.FragmentSwapListener;
import com.cherifcodes.bakingapp.adaptersAndListeners.IngredientAdapter;
import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.utils.JsonToObjects;
import com.cherifcodes.bakingapp.utils.ListProcessor;
import com.cherifcodes.bakingapp.viewModels.RecipeStepsActivityViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeIngredientsFragment extends Fragment {
    private Button mViewStepsBtn;
    private RecyclerView mRecyclerView;
    private FragmentSwapListener mFragmentSwapListener;

    private RecipeStepsActivityViewModel mViewModel;
    private IngredientAdapter mIngredientAdapter;

    public RecipeIngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_recipe_ingredients, container,
                false);
        mRecyclerView = fragmentLayout.findViewById(R.id.rclv_recipe_ingredients);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Retrieve recipeId and recipeName from the parent activity

        Bundle bundle = getActivity().getIntent().getExtras();
        this.getActivity().setTitle(bundle.getString(IntentConstants.RECIPE_NAME_KEY));
        int recipeId = bundle.getInt(IntentConstants.RECIPE_ID_KEY);
        // Get the current list of ingredients
        List<Ingredient> fullIngredientList = JsonToObjects.getIngredientList();
        List<Ingredient> currIngredientList = ListProcessor.getIngredientsById(fullIngredientList,
                recipeId);

        // Initialize the Ingredient list adapter and link it to the RecyclerView
        mIngredientAdapter = new IngredientAdapter();
        /*mIngredientAdapter.setIngredientList(currIngredientList);
        mRecyclerView.setAdapter(mIngredientAdapter);*/

        /*mViewModel = ViewModelProviders.of(this).get(RecipeStepsActivityViewModel.class);
        mViewModel.getAllIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredientList) {
                mIngredientAdapter.setIngredientList(ingredientList);
                mIngredientAdapter.notifyDataSetChanged();
            }
        });*/

        mIngredientAdapter.setIngredientList(currIngredientList);
        mRecyclerView.setAdapter(mIngredientAdapter);


        // Initialize the FragmentSwapListener and link it to button clicks
        mFragmentSwapListener = (FragmentSwapListener) getActivity();
        mViewStepsBtn = fragmentLayout.findViewById(R.id.btn_view_steps);
        mViewStepsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentSwapListener.onFragmentSwapped(RecipeStepsActivity
                        .STEPS_FRAGMENT);
            }
        });

        return fragmentLayout;
    }
}
