package com.cherifcodes.bakingapp.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repository {

    private static Context mContext;
    private static AppDatabase db;
    private static Executor mExecutor = Executors.newSingleThreadExecutor();

    private static Repository instance;

    private Repository() {
        db = AppDatabase.getInstance(mContext);
    }

    public static Repository getInstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = new Repository();
        }
        return instance;
    }

    public void insertIngredients(final Ingredient ingredient) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.getIngredientDao().insertIngredient(ingredient);
            }
        });
    }

    public LiveData<List<Ingredient>> getAllIngredients() {
        return db.getIngredientDao().getAllIngredients();
    }

    public void deleteAll() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.getIngredientDao().deleteAll();
            }
        });
    }
}
