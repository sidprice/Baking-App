package com.sidprice.android.baking_app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import com.sidprice.android.baking_app.adapters.CardsRecyclerViewAdatpter;
import com.sidprice.android.baking_app.data.RecipesViewModel;

import java.security.PublicKey;

public class RecipeListFragment extends Fragment {
    private static final String TAG = RecipeListFragment.class.getSimpleName();
    private RecyclerView                mCardRecyclerView ;
    private CardsRecyclerViewAdatpter   mCardsRecyclerViewAdapter;
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
        RecipesViewModel recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class) ;
        recipesViewModel.getRecipes().observe(this, recipes -> {
            //
            // Update the UI
            //
            if ( recipes != null )
            {
                Log.d(TAG, "onCreateView: Update the UI");
                if ( mCardsRecyclerViewAdapter == null ) {
                    mCardsRecyclerViewAdapter = new CardsRecyclerViewAdatpter(recipes) ;
                }
                mCardRecyclerView.setAdapter(mCardsRecyclerViewAdapter);
            }
        });

        return rootView;
    }
}
