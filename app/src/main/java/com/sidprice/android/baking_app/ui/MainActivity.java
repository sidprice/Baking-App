package com.sidprice.android.baking_app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.adapters.CardsRecyclerViewAdatpter;
import com.sidprice.android.baking_app.data.RecipesViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView    mCardRecyclerView ;
    private CardsRecyclerViewAdatpter mCardsRecyclerViewAdapter;
    private Context         mContext ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext() ;
        setContentView(R.layout.activity_main);
        //
        mCardRecyclerView = (RecyclerView)findViewById(R.id.cards_recycler_view) ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) ;
        mCardRecyclerView.setLayoutManager(linearLayoutManager);
        //
        RecipesViewModel    recipesViewModel = ViewModelProviders.of(this).get(RecipesViewModel.class) ;
        recipesViewModel.getRecipes().observe(this, recipes -> {
            //
            // Update the UI
            //
            if ( recipes != null )
            {
                Log.d(TAG, "onCreate: Update the UI");
                if ( mCardsRecyclerViewAdapter == null ) {
                    mCardsRecyclerViewAdapter = new CardsRecyclerViewAdatpter(recipes) ;
                }
                mCardRecyclerView.setAdapter(mCardsRecyclerViewAdapter);
            }
        });
    }
}
