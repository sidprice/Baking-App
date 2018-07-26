package com.sidprice.android.baking_app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sidprice.android.baking_app.R;

public class RecipeDetailsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
    }
}
