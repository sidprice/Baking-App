package com.sidprice.android.baking_app.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.adapters.CardsRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements CardsRecyclerViewAdapter.OnRecipeClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onSelectedRecipe(int position) {

    }
}
