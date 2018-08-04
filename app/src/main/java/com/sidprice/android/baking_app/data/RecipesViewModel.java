package com.sidprice.android.baking_app.data;
//
// Inspiration from:
//      https://medium.com/google-developers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4
//
import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.model.Ingredient;
import com.sidprice.android.baking_app.model.Recipe;
import com.sidprice.android.baking_app.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipesViewModel extends ViewModel {
    private static final String TAG = RecipesViewModel.class.getSimpleName();

    private final   String  recipes_url = "http://go.udacity.com/android-baking-app-json" ;
    //
    // json recipe file keys
    //
    private final String    JSON_RECIPE_ID = "id" ;
    private final String    JSON_RECIPE_NAME = "name" ;
    private final String    JSON_RECIPE_INGREDIENTS = "ingredients" ;
    private final String    JSON_RECIPE_STEPS = "steps" ;
    private final String    JSON_RECIPE_SERVINGS = "servings" ;
    private final String    JSON_RECIPE_IMAGE = "image" ;
    //
    // ingredient keys
    //
    private final String    JSON_INGREDIENT_QUANTTIY = "quantity" ;
    private final String    JSON_INGREDIENT_MEASURE = "measure" ;
    private final String    JSON_INGREDIENT = "ingredient" ;
    //
    // step keys
    //
    private final String    JSON_STEP_ID = "id" ;
    private final String    JSON_STEP_SHORT_DESCRIPTION = "shortDescription" ;
    private final String    JSON_STEP_DESCRIPTION = "description" ;
    private final String    JSON_STEP_VIDEO_URL = "videoURL" ;
    private final String    JSON_STEP_THUMBNAIL_URL = "thumbnailURL" ;

    private static MutableLiveData<ArrayList<Recipe>>   recipes ;

    public MutableLiveData<ArrayList<Recipe>>   getRecipes() {
        if ( recipes == null) {
            recipes = new MutableLiveData<ArrayList<Recipe>>() ;
            loadRecipes() ;
        }
        return recipes ;
    }

    private void loadRecipes() {
        //
        // Load the json recipes file
        //
        getJsonRecipes() ;
    }

    private void  getJsonRecipes() {
        //
        // Start the pricess of reading the JSON file
        //      It is completed when the AsyncTask has executed
        //
        try {
            byte[] resultBytes ;
                /*
                    Build the URI for the recipes list json file
                 */
            String url = Uri.parse(recipes_url)
                    .buildUpon()
                    .build().toString() ;
            getFileFromURL(url) ;
        }
        catch (IOException ex) {
            Log.d("getJsonRecipes: ",ex.toString() ) ;
        }
     return ;
    }

    private final Integer MAX_BUFFER_SIZE = 4096 ;

    @SuppressLint("StaticFieldLeak")
    private void getFileFromURL(String urlInput) throws IOException {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                String  jsonString = "" ;
                HttpURLConnection myConnection = null;
                try {
                    URL url = new URL(urlInput) ;
                    myConnection = (HttpURLConnection) url.openConnection();
                    //
                    // The URL is probably redirected so check it
                    //
                    String redirect = myConnection.getHeaderField("Location");
                    if (redirect != null){
                        myConnection = (HttpURLConnection) new URL(redirect).openConnection();
                    }

                    if ( myConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new IOException(myConnection.getResponseMessage()) ;
                    }

                    ByteArrayOutputStream bytesFromServer = new ByteArrayOutputStream() ;
                    InputStream input = myConnection.getInputStream() ;

                    Integer numberOfBytes = 0 ;
                    byte[] inputBuffer = new byte[MAX_BUFFER_SIZE] ;

                    while ( (numberOfBytes = input.read(inputBuffer)) > 0) {
                        bytesFromServer.write(inputBuffer, 0 , numberOfBytes) ;
                    }
                    jsonString = new String(bytesFromServer.toByteArray(), "UTF-8");
                    return jsonString ;
                } catch (IOException ex) {
                    Log.d(TAG, "doInBackground: " + ex.toString());
                }
                finally {
                    myConnection.disconnect(); ;
                }
                return jsonString ;
            }

            @Override
            protected void onPostExecute(String s) {
                //
                // Convert the result string to JSON object
                //
                try {
                    JSONArray jsonArray = new JSONArray(s) ;
                    Log.d(TAG, "onPostExecute: JSON recipes read and parsed to JSONArray");
                    //
                    // Parse into Recipes
                    //
                    ArrayList<Recipe>    new_recipes = new ArrayList<>();
                    Recipe          new_recipe = null;
                    Integer         recipe_count = jsonArray.length() ;
                    for ( int i = 0 ; i < recipe_count ; i++ ) {
                        //
                        new_recipe = new Recipe() ; // Create a new recipe to receive this data
                        //
                        // Set the values from the current json array element
                        //
                        JSONObject  jsonObject = jsonArray.getJSONObject(i) ;
                        //
                        new_recipe.setId(jsonObject.getInt(JSON_RECIPE_ID));    // Get the recipe id
                        // Name
                        if ( jsonObject.has(JSON_RECIPE_NAME)) { new_recipe.setName( jsonObject.getString(JSON_RECIPE_NAME)); }
                        else { new_recipe.setName(""); }
                        // Servings
                        if ( jsonObject.has(JSON_RECIPE_SERVINGS)) { new_recipe.setServings( jsonObject.getInt(JSON_RECIPE_SERVINGS)); }
                        else { new_recipe.setServings(1); }
                        // Image
                        if ( jsonObject.has(JSON_RECIPE_IMAGE)) { new_recipe.setImage( jsonObject.getString(JSON_RECIPE_IMAGE)); }
                        else { new_recipe.setImage(""); }
                        // Ingredients
                        if ( jsonObject.has(JSON_RECIPE_INGREDIENTS)) {
                            //
                            // Iterate over the array of ingredients
                            //
                            JSONArray   ingredientsArray = jsonObject.getJSONArray(JSON_RECIPE_INGREDIENTS) ;
                            ArrayList<Ingredient>    new_ingredients = new ArrayList<>() ;
                            //
                            for ( int j = 0 ; j < ingredientsArray.length() ; j++) {
                                Ingredient  new_ingredient = new Ingredient() ;
                                JSONObject  ingredientObject = ingredientsArray.getJSONObject(j) ;
                                //  Quantity
                                if ( ingredientObject.has(JSON_INGREDIENT_QUANTTIY)) { new_ingredient.setQuantity( ingredientObject.getDouble(JSON_INGREDIENT_QUANTTIY)); }
                                else { new_ingredient.setQuantity(0); }
                                //  Measure
                                if ( ingredientObject.has(JSON_INGREDIENT_MEASURE)) { new_ingredient.setMeasure( ingredientObject.getString(JSON_INGREDIENT_MEASURE)); }
                                else { new_ingredient.setMeasure(Resources.getSystem().getString(R.string.recipe_unknown_measure)); }
                                //  Quantity
                                if ( ingredientObject.has(JSON_INGREDIENT)) { new_ingredient.setIngredient( ingredientObject.getString(JSON_INGREDIENT)); }
                                else { new_ingredient.setIngredient(Resources.getSystem().getString(R.string.recipe_unknown_ingredient)); }
                                //
                                // Add to the list of ingredients
                                //
                                new_ingredients.add(new_ingredient) ;
                            }
                            //
                            // Add to the recipe
                            //
                            new_recipe.setIngredients(new_ingredients);
                        }
                        // Steps
                        if ( jsonObject.has(JSON_RECIPE_INGREDIENTS)) {
                            //
                            // Iterate over the array of steps
                            //
                            JSONArray   stepsArray = jsonObject.getJSONArray(JSON_RECIPE_STEPS) ;
                            ArrayList<Step>    new_steps = new ArrayList<>() ;
                            //
                            for ( int j = 0 ; j < stepsArray.length(); j++ ) {
                                Step  new_step = new Step() ;
                                JSONObject  stepObject = stepsArray.getJSONObject(j) ;
                                // id
                                new_step.setId( stepObject.getInt(JSON_STEP_ID));
                                // short description
                                if ( stepObject.has(JSON_STEP_SHORT_DESCRIPTION)) { new_step.setShort_description( stepObject.getString(JSON_STEP_SHORT_DESCRIPTION)); }
                                else { new_step.setShort_description(Resources.getSystem().getString(R.string.recipe_step_short_description_missing)); }
                                // description
                                if ( stepObject.has(JSON_STEP_DESCRIPTION)) { new_step.setDescription( stepObject.getString(JSON_STEP_DESCRIPTION)); }
                                else { new_step.setShort_description(Resources.getSystem().getString(R.string.recipe_step_description_missing)); }
                                // video URL
                                if ( stepObject.has(JSON_STEP_VIDEO_URL)) { new_step.setVideo_url( stepObject.getString(JSON_STEP_VIDEO_URL)); }
                                else { new_step.setVideo_url(""); }
                                // thumbnail URL
                                if ( stepObject.has(JSON_STEP_THUMBNAIL_URL)) { new_step.setThumbnail_url( stepObject.getString(JSON_STEP_THUMBNAIL_URL)); }
                                else { new_step.setThumbnail_url(""); }
                                //
                                // Add to list of steps
                                //
                                new_steps.add(new_step) ;
                            }
                            //
                            // Add steps to the recipe
                            //
                            new_recipe.setSteps(new_steps);
                        }
                        //
                        // Add the recipe to the list of recipes
                        //
                        if ( new_recipe != null ) {
                            new_recipes.add(new_recipe) ;
                        }
                    }
                    recipes.setValue(new_recipes);
                    //
                    // Add to repository
                    //
                    RecipeRepository recipeRepository = RecipeRepository.getInstance() ;
                    recipeRepository.setRecipes(recipes);


                } catch (JSONException ex ) {
                    Log.d(TAG, "onPostExecute:" + ex.getStackTrace());
                }
            }
        }.execute() ;
    }

}
