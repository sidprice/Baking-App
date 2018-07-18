package com.sidprice.android.baking_app;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sidprice.android.baking_app.data.RecipesViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipesViewModel    recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class) ;
        recipesViewModel.getRecipes().observe(this, recipes -> {
            //
            // Update the UI
            //
            Log.d(TAG, "onCreate: Update the UI");
        });
    }
}
