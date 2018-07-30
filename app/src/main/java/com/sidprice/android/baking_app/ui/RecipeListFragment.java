package com.sidprice.android.baking_app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.adapters.CardsRecyclerViewAdapter;
import com.sidprice.android.baking_app.data.RecipesViewModel;
import com.sidprice.android.baking_app.model.Recipe;

public class RecipeListFragment extends Fragment implements CardsRecyclerViewAdapter.OnRecipeClickListener {
    private static final String TAG = RecipeListFragment.class.getSimpleName();
    RecipesViewModel                    mRecipesViewModel ;
    private RecyclerView                mCardRecyclerView ;
    private CardsRecyclerViewAdapter    mCardsRecyclerViewAdapter;
    private Context                     mContext ;
    // Mandatory Empty Constructor
    public void RecipeListFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        // Inflate the recipe master list fragment layout
        //
        final View  rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false );
        mCardRecyclerView = (RecyclerView)rootView.findViewById(R.id.cards_recycler_view) ;
        mContext = getContext() ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) ;
        mCardRecyclerView.setLayoutManager(linearLayoutManager);
        mRecipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class) ;
        mRecipesViewModel.getRecipes().observe(this, recipes -> {
            //
            // Update the UI
            //
            if ( recipes != null )
            {
                Log.d(TAG, "onCreateView: Update the UI");
                if ( mCardsRecyclerViewAdapter == null ) {
                    mCardsRecyclerViewAdapter = new CardsRecyclerViewAdapter(getContext() , recipes, this::onSelectedRecipe) ;
                }
                mCardRecyclerView.setAdapter(mCardsRecyclerViewAdapter);
            }
        });

        return rootView;
    }

    @Override
    public void onSelectedRecipe(int position) {
        //
        // Launch the recipe details intent
        //
        // TODO  deal with tablet here
        //
        Intent intent = new Intent(getContext(), RecipeDetailsActivity.class) ;
        //
        // Add the selected recipe to the Intent extra data
        //
        Recipe  selectedRecipe = mRecipesViewModel.getRecipes().getValue().get(position) ;
        intent.putExtra(Recipe.RECIPE_PARCEL_KEY, selectedRecipe) ;
        startActivity(intent);
    }

}
