package com.cherifcodes.bakingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cherifcodes.bakingapp.adaptersAndListeners.FragmentSwapListener;
import com.cherifcodes.bakingapp.adaptersAndListeners.IngredientAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeIngredientsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FragmentSwapListener mFragmentSwapListener;
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
        // Initialize the Ingredient list adapter and link it to the RecyclerView
        mIngredientAdapter = new IngredientAdapter();

        // Retrieve recipeId and recipeName from the parent activity
        Bundle bundle = getActivity().getIntent().getExtras();
        this.getActivity().setTitle(bundle.getString(IntentConstants.RECIPE_NAME_KEY));

        // Initialize the FragmentSwapListener and link it to button clicks
        mFragmentSwapListener = (FragmentSwapListener) getActivity();


        mIngredientAdapter.setIngredientList(mFragmentSwapListener.getCurrIngredientList());
        mRecyclerView.setAdapter(mIngredientAdapter);

        Button viewStepsBtn = fragmentLayout.findViewById(R.id.btn_view_steps);
        Button saveIngredientsBtn = fragmentLayout.findViewById(R.id.btn_save_ingredients);
        viewStepsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentSwapListener.onFragmentSwapped(RecipeStepsActivity
                        .STEPS_FRAGMENT);
            }
        });

        saveIngredientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentSwapListener.onSaveIngredients();
                Toast.makeText(getActivity(), R.string.saved_ingredients_msg, Toast.LENGTH_LONG).show();
            }
        });

        return fragmentLayout;
    }
}
