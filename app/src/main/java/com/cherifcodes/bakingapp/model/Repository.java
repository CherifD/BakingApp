package com.cherifcodes.bakingapp.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Repository {

    private static Context mContext;
    private static AppDatabase db;
    private static Executor mExecutor = Executors.newSingleThreadExecutor();
    private long[] insertedRecords = new long[0];
    private static Repository instance;
    private List<Ingredient> mIngredientList;

    private Repository() {
        mIngredientList = new ArrayList<>();
        db = AppDatabase.getInstance(mContext);
    }

    public static Repository getInstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = new Repository();
        }
        return instance;
    }

    public long[] insertIngredients(final List<Ingredient> ingredientList) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                insertedRecords = db.getIngredientDao().insertAll(ingredientList);
                Log.i("repoInsert", " = " + insertedRecords.length);
            }
        });

        return insertedRecords;
    }

    public List<Ingredient> getAllIngredients() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mIngredientList = db.getIngredientDao().getAllIngredients();
                Log.i("RepoGet2", "list size = " + mIngredientList.size());
            }
        });

        return mIngredientList;
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
