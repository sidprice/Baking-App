package com.sidprice.android.baking_app.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.sidprice.android.baking_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance ;
    private MutableLiveData<ArrayList<Recipe>> recipes = new MutableLiveData<ArrayList<Recipe>>() ;

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    public void setRecipes(MutableLiveData<ArrayList<Recipe>> recipes) {
        this.recipes = recipes ;
    }

    public MutableLiveData<ArrayList<Recipe>> getRecipes() {
        return recipes ;
    }
}
