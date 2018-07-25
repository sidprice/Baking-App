package com.sidprice.android.baking_app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.model.Recipe;

import java.util.List;

public class CardsRecyclerViewAdatpter extends RecyclerView.Adapter<CardsRecyclerViewAdatpter.CardsViewHolder> {
    List<Recipe>    mRecipes ;
    public CardsRecyclerViewAdatpter(List<Recipe> recipes) {
        this.mRecipes = recipes ;
    }

    @NonNull
    @Override
    public CardsRecyclerViewAdatpter.CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View    v = LayoutInflater.from(parent.getContext()).inflate((R.layout.recipe_cardview_item), parent, false) ;
        CardsViewHolder cardsViewHolder = new CardsViewHolder(v) ;
        return cardsViewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsViewHolder holder, int position) {
        if ( mRecipes != null ) {
            if ( position < mRecipes.size() ) {
                Recipe  recipe = mRecipes.get(position) ;
                holder.mName_TextView.setText(recipe.getName());
                holder.mServings_TextView.setText("Serves " + recipe.getServings() + " people");
                //
                // TODO image addition
                //
               // holder.mImage_ImageView.setImageResource(R.drawable.silverware_fork_knife);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null ) {
            return 0 ;
        } else {
            return mRecipes.size() ;
        }
    }

    public static class CardsViewHolder extends RecyclerView.ViewHolder {
        private CardView    mCardView ;
        private TextView    mName_TextView ;
        private TextView    mServings_TextView ;
        private ImageView   mImage_ImageView ;

        public CardsViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cards_recycler_view) ;
            mName_TextView = (TextView) itemView.findViewById(R.id.recipe_name) ;
            mServings_TextView = (TextView) itemView.findViewById(R.id.recipe_servings) ;
            mImage_ImageView = (ImageView)itemView.findViewById(R.id.recipe_image) ;
        }
    }
}
