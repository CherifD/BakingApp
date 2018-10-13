package com.cherifcodes.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.cherifcodes.bakingapp.adaptersAndListeners.RecipeStepAdapter;
import com.cherifcodes.bakingapp.adaptersAndListeners.StepClickListener;
import com.cherifcodes.bakingapp.model.RecipeStep;
import com.cherifcodes.bakingapp.utils.JsonToObjects;
import com.cherifcodes.bakingapp.utils.ListProcessor;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsActivity extends AppCompatActivity implements StepClickListener {

    private List<RecipeStep> mRecipeStepList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecipeStepAdapter mRecipeStepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        mRecyclerView = findViewById(R.id.rclv_recipe_steps);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        int recipeId = bundle.getInt(IntentConstants.RECIPE_ID_KEY);
        mRecipeStepList = ListProcessor.getRecipeStepsById(JsonToObjects.getRecipeStepList(),
                recipeId);
        this.setTitle(bundle.getString(IntentConstants.RECIPE_NAME_KEY));

        mRecipeStepAdapter = new RecipeStepAdapter(mRecipeStepList, this);
        mRecyclerView.setAdapter(mRecipeStepAdapter);

    }

    @Override
    public void onStepClicked(int recipeStepId) {
        String stepVideoUrl = mRecipeStepList.get(recipeStepId).getVideoUrlStr();

        if (TextUtils.isEmpty(stepVideoUrl)) {
            Toast.makeText(this, "There is no video available for this step.",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, mRecipeStepList.get(recipeStepId).getVideoUrlStr(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
