package com.cherifcodes.bakingapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.cherifcodes.bakingapp.model.Ingredient;
import com.cherifcodes.bakingapp.model.Repository;

import java.util.List;

/**
 * This ViewModel class provides data to the RecipeStepsActivity and its Fragment classes
 */
public class RecipeStepsActivityViewModel extends AndroidViewModel {

    private LiveData<List<Ingredient>> mIngredientList;
    private Repository repository;

    public RecipeStepsActivityViewModel(Application application) {
        super(application);
        repository = Repository.getInstance(application);
        mIngredientList = repository.getAllIngredients();

        ///
        /*Log.i(RecipeStepsActivityViewModel.class.getSimpleName(), "listSize = " +
        mIngredientList.getValue().size());*/
    }

    public void insertIngredientList(Ingredient currIngredient) {
        repository.insertIngredients(currIngredient);
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return mIngredientList;
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
