package com.cherifcodes.bakingapp.adaptersAndListeners;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cherifcodes.bakingapp.R;
import com.cherifcodes.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> mRecipeList;
    private RecipeClickListener mRecipeClickListener;
    private Context mContext;

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
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        holder.recipeNameTv.setText(recipe.getRecipeName());
        holder.servingSizeTv.setText(String.valueOf(recipe.getServingSize()));

        // Use Picasso to load the recipe image
        try {
            Picasso.with(mContext)
                    .load(recipe.getImageUrlStr())
                    .into(holder.recipeImageImv);
        } catch (Exception e) {
            Log.i(this.getClass().getSimpleName(), "Invalid recipe image url.");
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList == null ? 0 : mRecipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeNameTv;
        TextView servingSizeTv;
        ImageView recipeImageImv;


        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeNameTv = itemView.findViewById(R.id.tv_recipeName);
            servingSizeTv = itemView.findViewById(R.id.tv_servingSize);
            recipeImageImv = itemView.findViewById(R.id.imv_recipeImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mRecipeClickListener.onRecipeClick(this.getAdapterPosition());
        }
    }
}
