package com.sidprice.android.baking_app.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.model.Recipe;
import com.sidprice.android.baking_app.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsRecyclerViewAdapter extends RecyclerView.Adapter<RecipeStepsRecyclerViewAdapter.StepViewHolder> {
    private Recipe  mRecipe ;
    private OnStepClickListener mCallback ;

    public RecipeStepsRecyclerViewAdapter( Recipe recipe, OnStepClickListener callback ) {
        mRecipe = recipe ;
        mCallback = callback ;
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
                    holder.mItemContainer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCallback.onSelectedStep(position); ;
                        }
                    });
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
        @BindView(R.id.step_short_description) TextView    mShortDescription_tv ;
        @BindView(R.id.step_description) TextView    mDescription_tv ;
        @BindView(R.id.step_item_container) ConstraintLayout mItemContainer ;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView) ;
        }
    }
}
