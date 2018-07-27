package com.sidprice.android.baking_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.model.Ingredient;

import java.util.List;

public class RecipeIngredientsAdaptor extends RecyclerView.Adapter<RecipeIngredientsAdaptor.IngredientsViewHolder> {
    private List<Ingredient>    mIngredients ;
    private Context             mContext ;

    public RecipeIngredientsAdaptor(List<Ingredient> ingredients ) {
        this.mIngredients = ingredients ;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext() ;
        View    v = LayoutInflater.from(parent.getContext()).inflate((R.layout.item_ingredient), parent, false) ;
        IngredientsViewHolder ingredientsViewHolder = new IngredientsViewHolder(v) ;
        return ingredientsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        if ( mIngredients != null ) {
            if ( position < mIngredients.size() ) {
                holder.mDescription_TextView.setText((position+1) + "\t " + mIngredients.get(position).getIngredient() );
            }
        }
    }

    @Override
    public int getItemCount() {
        if ( mIngredients == null ) {
            return 0 ;
        } else {
            return mIngredients.size() ;
        }
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        private TextView    mDescription_TextView ;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            mDescription_TextView = itemView.findViewById(R.id.ingredient_description) ;
        }
    }
}
