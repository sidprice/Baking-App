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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.adapters.RecipeIngredientsAdaptor;
import com.sidprice.android.baking_app.data.RecipesViewModel;
import com.sidprice.android.baking_app.model.Ingredient;
import com.sidprice.android.baking_app.model.Recipe;

import java.util.List;

public class RecipeDetailFragment extends Fragment {
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private RecyclerView    mIngredientsRecyclerView ;
    private RecyclerView    mStepsDescriptionRecyclerView ;

    private RecipeIngredientsAdaptor    mRecipeIngredientsAdapter ;

    private Context         mContext ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View  rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false) ;
        mContext = getContext() ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) ;
        mIngredientsRecyclerView = (RecyclerView)rootView.findViewById(R.id.recipe_ingredients_recycler_view) ;
        mIngredientsRecyclerView.setLayoutManager(linearLayoutManager);
        RecipesViewModel recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class) ;

        Intent  intent = getActivity().getIntent() ;
        Recipe  recipe = intent.getExtras().getParcelable("Recipe") ;
        //
        mRecipeIngredientsAdapter = new RecipeIngredientsAdaptor(recipe.getIngredients()) ;
        mIngredientsRecyclerView.setAdapter(mRecipeIngredientsAdapter);
        return rootView ;
    }
}
