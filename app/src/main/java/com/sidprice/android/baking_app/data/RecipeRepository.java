package com.sidprice.android.baking_app.data;

import com.sidprice.android.baking_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {

    private static RecipeRepository instance ;
    private List<Recipe> recipes = new ArrayList<>();

    public static RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    public void setRecipes(List recipes) {
        this.recipes = recipes ;
    }

    public List<Recipe> getRecipes() {
        return recipes ;
    }
}
