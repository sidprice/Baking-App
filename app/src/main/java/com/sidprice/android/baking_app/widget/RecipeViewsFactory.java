package com.sidprice.android.baking_app.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.sidprice.android.baking_app.data.RecipeRepository;
import com.sidprice.android.baking_app.model.Ingredient;
import com.sidprice.android.baking_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private int recipeId;
    private List<String> ingredients = new ArrayList<>();

    RecipeViewsFactory(Context context, int recipeId) {
        this.context = context;
        this.recipeId = recipeId;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        RecipeRepository    recipeRepository = RecipeRepository.getInstance() ;
        Recipe recipe = recipeRepository.getRecipes().getValue().get(recipeId) ;
        List<Ingredient>    ingredientsList = recipe.getIngredients() ;
        ingredients.clear();
        int ingredient_number = 1 ;
        for ( Ingredient ingredient : ingredientsList) {
            ingredients.add( ingredient_number + ". " + ingredient.getIngredient() ) ;
            ingredient_number++ ;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if ( ingredients == null ) {
            return 0 ;
        } else {
            return ingredients.size() ;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String ingredientString = ingredients.get(position);
        final RemoteViews views =
                new RemoteViews(context.getPackageName(), android.R.layout.simple_list_item_1);
        views.setTextViewText(android.R.id.text1, ingredientString);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
