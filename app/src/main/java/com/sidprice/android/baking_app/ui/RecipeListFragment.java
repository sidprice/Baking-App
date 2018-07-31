package com.sidprice.android.baking_app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.adapters.CardsRecyclerViewAdapter;
import com.sidprice.android.baking_app.data.RecipesViewModel;
import com.sidprice.android.baking_app.model.Recipe;

public class RecipeListFragment extends Fragment implements CardsRecyclerViewAdapter.OnRecipeClickListener {
    private static final String TAG = RecipeListFragment.class.getSimpleName();
    RecipesViewModel                    mRecipesViewModel ;
    private RecyclerView                mCardRecyclerView ;
    private CardsRecyclerViewAdapter    mCardsRecyclerViewAdapter;
    private RecipeDetailFragment        mRecipeDetailFragment ;
    private Context                     mContext ;
    private boolean                     mTwoPaneLayout ;
    // Mandatory Empty Constructor
    public void RecipeListFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        // Inflate the recipe master list fragment layout
        //
        final View  rootView  = inflater.inflate(R.layout.fragment_recipe_list, container, false );
        mCardRecyclerView = (RecyclerView)rootView.findViewById(R.id.cards_recycler_view) ;
        mContext = getContext() ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) ;
        mCardRecyclerView.setLayoutManager(linearLayoutManager);
        mRecipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class) ;
        //
        // Are we in two-pane layout?
        //
        View    view =  (View) getActivity().findViewById(R.id.baking_app_twopane_layout) ;
        if ( view != null ) {
            //
            // In two pane mode
            //
            mTwoPaneLayout = true ;
        } else  {
            mTwoPaneLayout = false ;
        }
        mRecipesViewModel.getRecipes().observe(this, recipes -> {
            //
            // Make sure the deatils fragment is instantiated
            //
            if ( mTwoPaneLayout ) {
                if (mRecipeDetailFragment == null ) {
                    Bundle  bundle = new Bundle() ;
                    bundle.putParcelable(Recipe.RECIPE_PARCEL_KEY , recipes.get(0));
                    //
                    // We need to add the recipe details fragment
                    //
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager() ;
                    mRecipeDetailFragment = new RecipeDetailFragment() ;
                    mRecipeDetailFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_details_container, mRecipeDetailFragment)
                            .commit() ;
                    //mRecipeDetailFragment.setRecipe(recipes.get(0)) ;
                }

            }
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
//                if ( mRecipeDetailFragment != null ) {
//                    mRecipeDetailFragment.setRecipe(recipes.get(0)) ;
//                }
            }
        });

        return rootView;
    }

    @Override
    public void onSelectedRecipe(int position) {
        Recipe  selectedRecipe = mRecipesViewModel.getRecipes().getValue().get(position) ;
        if ( mTwoPaneLayout == true) {
            mRecipeDetailFragment.setRecipe(selectedRecipe);
        } else {
            //
            // Launch the recipe details intent
            //
            Intent intent = new Intent(getContext(), RecipeDetailsActivity.class) ;
            //
            // Add the selected recipe to the Intent extra data
            //
            intent.putExtra(Recipe.RECIPE_PARCEL_KEY, selectedRecipe) ;
            startActivity(intent);
        }
    }

    public void setTwoPaneLayout() {
        mTwoPaneLayout = true ;
    }
}
