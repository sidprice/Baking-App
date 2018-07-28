package com.sidprice.android.baking_app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.model.Recipe;
import com.sidprice.android.baking_app.model.Step;

import java.util.List;

public class RecipeStepsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeStepsRecyclerViewAdapter.StepViewHolder> {
    private Recipe  mRecipe ;

    public RecipeStepsRecyclerViewAdapter( Recipe recipe ) {
        mRecipe = recipe ;
    }
    public interface OnStepClickListener {
        void onSelectedStep(int position) ;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_step, parent, false) ;
        StepViewHolder stepViewHolder = new StepViewHolder(v) ;
        return stepViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        if ( mRecipe != null ) {
            List<Step> steps = mRecipe.getSteps() ;
            if ( steps != null )
            {
                if ( steps.size() > 0 ) {
                    Step step = steps.get(position) ;
                    holder.mShortDescription_tv.setText(step.getShort_description()) ;
                    holder.mDescription_tv.setText(step.getDescription());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if ( mRecipe != null ) {
            if ( mRecipe.getSteps() != null ) {
                return mRecipe.getSteps().size() ;
            }
        }
        return 0 ;
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        private TextView    mShortDescription_tv ;
        private TextView    mDescription_tv ;
        public StepViewHolder(View itemView) {
            super(itemView);
            mShortDescription_tv = (TextView)itemView.findViewById(R.id.step_short_description) ;
            mDescription_tv = (TextView)itemView.findViewById(R.id.step_description) ;
        }
    }
}
