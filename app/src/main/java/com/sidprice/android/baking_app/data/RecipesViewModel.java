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

    private static MutableLiveData<ArrayList<Recipe>>   recipes ;
    private RecipeRepository    mRespository ;

    public RecipesViewModel() {
        mRespository = RecipeRepository.getInstance() ;     // Will load data if not already loaded
    }
    public MutableLiveData<ArrayList<Recipe>>   getRecipes() {
        return mRespository.getRecipes() ;
    }


}
