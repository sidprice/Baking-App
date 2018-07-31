package com.sidprice.android.baking_app.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.adapters.CardsRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View    test = findViewById(R.id.baking_app_twopane_layout) ;
        if ( test != null ) {
            //
            // Two pane layout
            //
            FragmentManager fragmentManager = getSupportFragmentManager() ;
            //
            // Recipe List Fragment
            //
            RecipeListFragment  listFragment = new RecipeListFragment() ;
            //listFragment.setTwoPaneLayout();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_list_container, listFragment)
                    .commit() ;
        }

    }

}
