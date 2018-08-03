package com.sidprice.android.baking_app;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.sidprice.android.baking_app.data.RecipeRepository;
import com.sidprice.android.baking_app.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * The configuration screen for the {@link BakingTimeAppWidgetProvider BakingTimeAppWidgetProvider} AppWidget.
 */
public class BakingTimeAppWidgetProviderConfigureActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String PREFS_NAME = "com.sidprice.android.baking_app.BakingTimeAppWidgetProvider";
    private static final String PREF_PREFIX_KEY = "baking_time_";
    private static final String PREF_RECIPE_ID = PREF_PREFIX_KEY + "_id_" ;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private Spinner mSpinner ;
    private List<Recipe>    mRecipes ;
    private int             mSelecetdRecipe ;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = BakingTimeAppWidgetProviderConfigureActivity.this;
            //
            // Save the recipe ID in the prefs
            //
            int     recipeId = mSpinner.getSelectedItemPosition() ;
            saveRecipeId(context, mAppWidgetId, recipeId);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            BakingTimeAppWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public BakingTimeAppWidgetProviderConfigureActivity() {
        super();
    }

    // Write the recipe name and ingredients to the SharedPreferences object for this widget
    static void saveRecipeId(Context context, int appWidgetId, int recipeID) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_RECIPE_ID + appWidgetId, recipeID);
        prefs.apply();
    }

    // Read the recipe name from the SharedPreferences
    // object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadRecipeId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int recipeId = prefs.getInt(PREF_RECIPE_ID + appWidgetId, 0);
        return recipeId ;
    }


    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.baking_time_app_widget_provider_configure);

        //
        // Get recipes from repository
        //
        RecipeRepository    recipeRepository = RecipeRepository.getInstance() ;
        mRecipes = recipeRepository.getRecipes() ;
        //
        // Create the array of recipe names for display
        //
        ArrayList<String>    namesList = new ArrayList<>() ;

        for ( Recipe recipe : mRecipes ) {
            namesList.add(recipe.getName()) ;
        }
        String[] recipeNames = new String[namesList.size()] ;
        recipeNames = namesList.toArray(recipeNames) ;
        mSpinner = findViewById(R.id.appwidget_spinner) ;
        mSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, recipeNames) ;
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mSpinner.setAdapter(adapter);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        //mAppWidgetText.setText(loadTitlePref(BakingTimeAppWidgetProviderConfigureActivity.this, mAppWidgetId));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

