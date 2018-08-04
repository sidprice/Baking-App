package com.sidprice.android.baking_app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.data.RecipeRepository;
import com.sidprice.android.baking_app.model.Recipe;
import com.sidprice.android.baking_app.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BakingTimeAppWidgetProviderConfigureActivity BakingTimeAppWidgetProviderConfigureActivity}
 */
public class BakingTimeAppWidgetProvider extends AppWidgetProvider {
    static final String WIDGET_RECIPE_ID = "recipe_widget_id" ;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        int recipeId = BakingTimeAppWidgetProviderConfigureActivity.loadRecipeId(context, appWidgetId);
        ArrayList<Recipe> recipes = RecipeRepository.getInstance().getRecipes().getValue() ;
        Recipe recipe = recipes.get(recipeId) ;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_time_app_widget_provider);
        views.setTextViewText(R.id.appwidget_name, recipe.getName());
        Intent remoteViewsIntent = new Intent(context, RecipeWidgetService.class);
        remoteViewsIntent.putExtra(WIDGET_RECIPE_ID, recipeId);
        views.setRemoteAdapter(R.id.appwidget_ingredients, remoteViewsIntent);

        Intent appIntent = new Intent(context, MainActivity.class);
        // appIntent.putExtra(RECIPE_ID, recipeId);

        PendingIntent
                appPendingIntent = PendingIntent.getActivity(context, appWidgetId, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.appwidget_name, appPendingIntent);
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

