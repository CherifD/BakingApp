package com.cherifcodes.bakingapp.adaptersAndListeners;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherifcodes.bakingapp.R;
import com.cherifcodes.bakingapp.model.RecipeStep;

import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepHolder> {

    private List<RecipeStep> mRecipeStepList;
    private StepClickListener mStepClickListener;

    public RecipeStepAdapter(@NonNull List<RecipeStep> recipeStepList, StepClickListener stepClickListener) {
        mRecipeStepList = recipeStepList;
        mStepClickListener = stepClickListener;
    }


    @NonNull
    @Override
    public RecipeStepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(R.layout.recipe_step_item, parent, false);
        return new RecipeStepHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepHolder holder, int position) {
        RecipeStep recipeStep = mRecipeStepList.get(position);

        holder.shortDescription_tv.setText(recipeStep.getShortDescription());
        holder.description_tv.setText(recipeStep.getDescription());
    }

    @Override
    public int getItemCount() {
        return mRecipeStepList == null ? 0 : mRecipeStepList.size();
    }

    class RecipeStepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView shortDescription_tv;
        private TextView description_tv;

        public RecipeStepHolder(View itemView) {
            super(itemView);
            description_tv = itemView.findViewById(R.id.tv_description);
            shortDescription_tv = itemView.findViewById(R.id.tv_shortDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mStepClickListener.onStepClicked(getAdapterPosition());
        }

    }
}
