package com.cherifcodes.bakingapp.viewModels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class RecipeStepsActivityViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return null;
    }

    /*private final Context mContext;
    private int mRecipeId;

    public RecipeStepsActivityViewModelFactory(Context context, int recipeId) {
        mContext = context;
        mRecipeId = recipeId;
    }

    @NonNull
    @Override
    public RecipeStepsActivityViewModel create(@NonNull Class modelClass) {
        return new RecipeStepsActivityViewModel(mContext, mRecipeId);
    }*/
}
