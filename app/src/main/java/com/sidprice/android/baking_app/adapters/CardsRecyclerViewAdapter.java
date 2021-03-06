package com.sidprice.android.baking_app.adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardsRecyclerViewAdapter extends RecyclerView.Adapter<CardsRecyclerViewAdapter.CardsViewHolder> {
    List<Recipe>            mRecipes ;
    OnRecipeClickListener   mCallback ;
    Context                 mContext ;

    public CardsRecyclerViewAdapter( Context context, List<Recipe> recipes, OnRecipeClickListener callback) {
        this.mRecipes = recipes ;
        mContext    = context ;
        mCallback   = callback ;
    }

    @NonNull
    @Override
    public CardsRecyclerViewAdapter.CardsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View    v = LayoutInflater.from(parent.getContext()).inflate((R.layout.item_recipe), parent, false) ;
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
                if ( recipe.getImage().equals("")) {
                    holder.mImage_ImageView.setImageResource(R.drawable.cake_making);
                } else {
                    Picasso.get().load(recipe.getImage()).into(holder.mImage_ImageView);
                }
                //
                // Set click listener
                //
                holder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCallback.onSelectedRecipe(position);
                    }
                });
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
    //
    // Define an interface for the host activity to use to capture clicks
    // on a recipe, this method uses a callback in the host activity
    //
    public interface OnRecipeClickListener {
        void onSelectedRecipe(int position ) ;
    }

    public static class CardsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_card_view)    CardView   mCardView ;
        @BindView(R.id.recipe_name)         TextView    mName_TextView ;
        @BindView(R.id.recipe_servings)     TextView    mServings_TextView ;
        @BindView(R.id.recipe_image)        ImageView   mImage_ImageView ;

        public CardsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView) ;
        }
    }
}
