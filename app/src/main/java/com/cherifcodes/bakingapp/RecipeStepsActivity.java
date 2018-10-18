package com.cherifcodes.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RecipeStepsActivity extends AppCompatActivity /*implements StepClickListener*/ {

    /*private List<RecipeStep> mRecipeStepList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecipeStepAdapter mRecipeStepAdapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        /*mRecyclerView = findViewById(R.id.rclv_recipe_steps);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        int recipeId = bundle.getInt(IntentConstants.RECIPE_ID_KEY);
        mRecipeStepList = ListProcessor.getRecipeStepsById(JsonToObjects.getRecipeStepList(),
                recipeId);
        this.setTitle(bundle.getString(IntentConstants.RECIPE_NAME_KEY));

        mRecipeStepAdapter = new RecipeStepAdapter(mRecipeStepList, this);
        mRecyclerView.setAdapter(mRecipeStepAdapter);*/

    }

    /*@Override
    public void onStepClicked(int recipeStepId) {
        String stepVideoUrl = mRecipeStepList.get(recipeStepId).getVideoUrlStr();

        if (TextUtils.isEmpty(stepVideoUrl)) {
            Toast.makeText(this, "There is no video available for this step.",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra(IntentConstants.VIDEO_URL_KEY,
                    mRecipeStepList.get(recipeStepId).getVideoUrlStr());
            intent.putExtra(IntentConstants.STEP_DESCRIPTION_KEY,
                    mRecipeStepList.get(recipeStepId).getDescription());
            startActivity(intent);
        }
    }*/
}
