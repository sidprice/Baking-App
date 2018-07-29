package com.sidprice.android.baking_app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.model.Recipe;
import com.sidprice.android.baking_app.model.Step;

public class RecipeStepFragment extends Fragment {
    private Context     mContext ;
    private TextView    mShortDescription_tv ;
    private TextView    mDescription_tv ;
    private SimpleExoPlayerView mVideoPlayer ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext() ;
        Intent intent = getActivity().getIntent() ;
        Step step = intent.getExtras().getParcelable(Step.STEP_PARCEL_KEY) ;

        final View  rootView = inflater.inflate(R.layout.fragment_step_detail, container, false) ;
        mShortDescription_tv = (TextView)rootView.findViewById(R.id.step_short_description) ;
        mDescription_tv = (TextView)rootView.findViewById(R.id.step_description) ;
        mVideoPlayer = (SimpleExoPlayerView)rootView.findViewById(R.id.step_video_player) ;
        //
        UpdateUI(step) ;
        return rootView;
    }

    private void UpdateUI(Step step) {
        mShortDescription_tv.setText(step.getShort_description());
        mDescription_tv.setText(step.getDescription());
        //
        // TODO play the video if there is one
        //
    }
}
