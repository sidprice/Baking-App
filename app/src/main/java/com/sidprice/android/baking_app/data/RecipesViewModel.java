package com.sidprice.android.baking_app.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;
import android.util.Log;

import com.sidprice.android.baking_app.model.Recipe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.util.List;

public class RecipesViewModel extends ViewModel {
    private static final String TAG = RecipesViewModel.class.getSimpleName();

    private final   String  recipes_url = "http://go.udacity.com/android-baking-app-json" ;

    private MutableLiveData<List<Recipe>>   recipes ;

    public LiveData<List<Recipe>>   getRecipes() {
        if ( recipes == null) {
            recipes = new MutableLiveData<List<Recipe>>() ;
            loadRecipes() ;
        }
        return recipes ;
    }

    private void loadRecipes() {
        JSONObject  jsonRecipes = null ;
        //
        // Load the json recipes file
        //
        String      jsonString = getJsonRecipes() ;
        if ( jsonString != null ) {
            //
            // Initialize a json objecy for the recipes
            //
            try {
                jsonRecipes = new JSONObject(jsonString) ;

            } catch (JSONException ex ) {

            }
        }
    }

    private String  getJsonRecipes() {
        String jsonReturned = null ;
        try {
            byte[] resultBytes ;
                /*
                    Build the URI for the recipes list json file
                 */
            String url = Uri.parse(recipes_url)
                    .buildUpon()
                    .build().toString() ;
            resultBytes = getHTTPBytesFromURL(url) ;
            if ( resultBytes != null )
            {
                jsonReturned =  new String(resultBytes) ;
            }
        }
        catch (IOException ex) {
            Log.d("getJsonRecipes: ",ex.toString() + "\n") ;
        }
     return jsonReturned ;
    }

    private final Integer MAX_BUFFER_SIZE = 4096 ;

    private byte[] getHTTPBytesFromURL (String urlInput) throws IOException {
        URL url = new URL(urlInput) ;
        HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream bytesFromServer = new ByteArrayOutputStream() ;
            InputStream input = myConnection.getInputStream() ;

            if ( myConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(myConnection.getResponseMessage()) ;
            }

            Integer numberOfBytes = 0 ;
            byte[] inputBuffer = new byte[MAX_BUFFER_SIZE] ;
            while ( (numberOfBytes = input.read(inputBuffer)) > 0) {
                bytesFromServer.write(inputBuffer, 0 , numberOfBytes) ;
            }
            return bytesFromServer.toByteArray() ;
        } catch (IOException ex) {
            Log.e("something", ex.toString()) ;
            myConnection.disconnect();
            return null ;
        }
        finally {
            myConnection.disconnect(); ;
        }
    }

}
