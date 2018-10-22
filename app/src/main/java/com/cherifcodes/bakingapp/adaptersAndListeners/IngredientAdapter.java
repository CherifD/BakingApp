package com.cherifcodes.bakingapp.adaptersAndListeners;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cherifcodes.bakingapp.R;
import com.cherifcodes.bakingapp.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Ingredient> mIngredientList;

    public IngredientAdapter() {
        mIngredientList = new ArrayList<>();
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        mIngredientList = ingredientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.recipe_ingredient_item, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = mIngredientList.get(position);
        holder.mIngredientNameTv.setText(ingredient.getIngredientName());
        holder.mIngredientQuantityTv.setText(String.valueOf(ingredient.getQuantity()));
        holder.mIngredientUnitTv.setText(ingredient.getMeasureUnit());
    }

    @Override
    public int getItemCount() {
        return mIngredientList == null ? 0 : mIngredientList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mIngredientNameTv;
        private TextView mIngredientQuantityTv;
        private TextView mIngredientUnitTv;

        public ViewHolder(View itemView) {
            super(itemView);

            mIngredientNameTv = itemView.findViewById(R.id.tv_ingredient_name);
            mIngredientQuantityTv = itemView.findViewById(R.id.tv_ingredient_quantity);
            mIngredientUnitTv = itemView.findViewById(R.id.tv_ingredient_unit);
        }
    }
}
