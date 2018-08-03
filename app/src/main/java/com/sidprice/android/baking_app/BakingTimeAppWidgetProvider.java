package com.sidprice.android.baking_app;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.sidprice.android.baking_app.data.RecipeRepository;
import com.sidprice.android.baking_app.model.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BakingTimeAppWidgetProviderConfigureActivity BakingTimeAppWidgetProviderConfigureActivity}
 */
public class BakingTimeAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        int recipeId = BakingTimeAppWidgetProviderConfigureActivity.loadRecipeId(context, appWidgetId);
        List<Recipe> recipes = RecipeRepository.getInstance().getRecipes() ;
        Recipe recipe = recipes.get(recipeId) ;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_app_widget_provider);
        views.setTextViewText(R.id.appwidget_text, recipe.getName());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BakingTimeAppWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

