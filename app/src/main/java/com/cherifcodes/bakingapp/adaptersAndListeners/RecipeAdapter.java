package com.cherifcodes.bakingapp.adaptersAndListeners;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherifcodes.bakingapp.R;
import com.cherifcodes.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> mRecipeList;
    private RecipeClickListener mRecipeClickListener;

    public RecipeAdapter(RecipeClickListener recipeClickListener) {
        mRecipeClickListener = recipeClickListener;
        mRecipeList = new ArrayList<>();
    }

    public void setRecipeList(List<Recipe> recipeList) {
        mRecipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        holder.recipeNameTv.setText(recipe.getRecipeName());
        holder.servingSizeTv.setText(recipe.getServingSize() + "");
    }

    @Override
    public int getItemCount() {
        return mRecipeList == null ? 0 : mRecipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeNameTv;
        TextView servingSizeTv;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeNameTv = itemView.findViewById(R.id.tv_recipeName);
            servingSizeTv = itemView.findViewById(R.id.tv_servingSize);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mRecipeClickListener.onRecipeClick(this.getAdapterPosition());
        }
    }
}
