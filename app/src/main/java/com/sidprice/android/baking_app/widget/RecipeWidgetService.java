package com.sidprice.android.baking_app.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import static com.sidprice.android.baking_app.widget.BakingTimeAppWidgetProvider.WIDGET_RECIPE_ID;

public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int recipeId = intent.getIntExtra(WIDGET_RECIPE_ID, 0);
        return new RecipeViewsFactory(getApplicationContext(), recipeId);
    }
}
