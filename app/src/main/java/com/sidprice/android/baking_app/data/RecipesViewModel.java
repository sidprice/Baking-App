package com.sidprice.android.baking_app.data;
//
// Inspiration from:
//      https://medium.com/google-developers/lifecycle-aware-data-loading-with-android-architecture-components-f95484159de4
//
import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sidprice.android.baking_app.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    private MutableLiveData<List<Recipe>>   recipes ;

    public LiveData<List<Recipe>>   getRecipes() {
        if ( recipes == null) {
            recipes = new MutableLiveData<List<Recipe>>() ;
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
                    List<Recipe>    new_recipes ;
                    Recipe          new_recipe ;
                    Integer         recipe_count = jsonArray.length() ;
                    for ( int i = 0 ; i < recipe_count ; i++ ) {
                        //
                        // Create a new recipe to receive this data
                        //
                        new_recipe = new Recipe() ;
                        //
                        // Set the values from the current json array element
                        //
                        JSONObject  jsonObject = jsonArray.getJSONObject(i) ;
                        //
                        // Get the recipe id
                        //


                    }

                } catch (JSONException ex ) {
                    Log.d(TAG, "onPostExecute:" + ex.getStackTrace());
                }
            }
        }.execute() ;
    }

}
