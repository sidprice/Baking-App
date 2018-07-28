package com.sidprice.android.baking_app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.data.RecipesViewModel;
import com.sidprice.android.baking_app.model.Recipe;

public class RecipeDetailFragment extends Fragment {
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private TextView    mRecipeName_tv ;
    private TextView    mServings_tv ;
    private TextView    mIngredients_tv ;

    private RecyclerView    mStepsDescriptionRecyclerView ;

    private Context         mContext ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View  rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false) ;
        mRecipeName_tv = (TextView)rootView.findViewById(R.id.recipe_detail_name) ;
        mServings_tv = (TextView)rootView.findViewById(R.id.recipe_detail_servings) ;
        mIngredients_tv = (TextView)rootView.findViewById(R.id.recipe_detail_ingredients) ;
        mContext = getContext() ;

        RecipesViewModel recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class) ;

        Intent  intent = getActivity().getIntent() ;
        Recipe  recipe = intent.getExtras().getParcelable("Recipe") ;
        //
        updateUI(recipe) ;
        return rootView ;
    }

    private void updateUI(Recipe recipe) {
        mRecipeName_tv.setText(recipe.getName());
        mServings_tv.setText("Serves: " + recipe.getServings());
        mIngredients_tv.setText(recipe.getIngredientsString());
    }
}
