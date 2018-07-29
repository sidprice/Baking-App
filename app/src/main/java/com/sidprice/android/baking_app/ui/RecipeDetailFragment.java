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
import android.widget.ScrollView;
import android.widget.TextView;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.adapters.RecipeStepsRecyclerViewAdapter;
import com.sidprice.android.baking_app.data.RecipesViewModel;
import com.sidprice.android.baking_app.model.Recipe;

public class RecipeDetailFragment extends Fragment implements RecipeStepsRecyclerViewAdapter.OnStepClickListener {
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private TextView    mRecipeName_tv ;
    private TextView    mServings_tv ;
    private TextView    mIngredients_tv ;
    private ScrollView  mScrollView ;

    private RecyclerView                    mStepsDescriptionRecyclerView ;
    private RecipeStepsRecyclerViewAdapter  mStepsAdapter ;
    private RecyclerView.LayoutManager      mLayoutManager ;

    private Context         mContext ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext() ;
        Intent  intent = getActivity().getIntent() ;
        Recipe  recipe = intent.getExtras().getParcelable(Recipe.RECIPE_PARCEL_KEY) ;

        final View  rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false) ;
        mRecipeName_tv = (TextView)rootView.findViewById(R.id.recipe_detail_name) ;
        mServings_tv = (TextView)rootView.findViewById(R.id.recipe_detail_servings) ;
        mIngredients_tv = (TextView)rootView.findViewById(R.id.recipe_detail_ingredients) ;
        mStepsDescriptionRecyclerView = (RecyclerView)rootView.findViewById(R.id.recipe_step_detail_recycler_view) ;
        mLayoutManager = new LinearLayoutManager(mContext) ;
        mStepsDescriptionRecyclerView.setLayoutManager(mLayoutManager);
        mStepsAdapter = new RecipeStepsRecyclerViewAdapter(recipe, this::onSelectedStep) ;
        mStepsDescriptionRecyclerView.setAdapter(mStepsAdapter);
        mScrollView = (ScrollView)rootView.findViewById(R.id.recipe_details_scrollview) ;
        //
        updateUI(recipe) ;
        return rootView ;
    }

    private void updateUI(Recipe recipe) {
        if ( recipe != null ) {
            mRecipeName_tv.setText(recipe.getName());
            mServings_tv.setText("Serves: " + recipe.getServings());
            mIngredients_tv.setText(recipe.getIngredientsString());
            mScrollView.fullScroll(ScrollView.FOCUS_UP) ;
        }
    }

    @Override
    public void onSelectedStep(int position) {

    }
}
