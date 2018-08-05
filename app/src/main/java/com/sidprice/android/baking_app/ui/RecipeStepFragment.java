package com.sidprice.android.baking_app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.model.Recipe;
import com.sidprice.android.baking_app.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment {
    private static final String TAG = RecipeStepFragment.class.getSimpleName();
    private static final String RECIPE_CURRENT_STEP = "RecipeCurrentStep" ;

    private Context     mContext ;
    @BindView(R.id.step_description)        TextView    mDescription_tv ;
    @BindView(R.id.step_video_player)       SimpleExoPlayerView mPlayerView;
    @BindView(R.id.step_no_video_image)     TextView     mPlayerNotAvailable;
    @BindView(R.id.step_next)               Button      mNextButton ;
    @BindView(R.id.step_previous)           Button      mPreviousButton ;

    private SimpleExoPlayer     mExoPlayer ;
    private int         mCurrentStep ;
    private Recipe      mRecipe ;
    private boolean     mTwoPaneMode ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View  rootView ;
        mContext = getContext() ;
        Activity activity = getActivity() ;
        Intent intent = activity.getIntent() ;
        mRecipe = intent.getExtras().getParcelable(Recipe.RECIPE_PARCEL_KEY) ;
        mCurrentStep = intent.getIntExtra(Recipe.RECIPE_SELECTED_STEP, 0) ;
        mTwoPaneMode = intent.getBooleanExtra(Recipe.RECIPE_TWO_PANE_MODE, false) ;
        //
        // Are we in tablet mode?
        //
        if ( mTwoPaneMode == true ) {
            rootView = inflater.inflate(R.layout.fragment_step_detail_tablet, container, false) ;
        } else {
            rootView = inflater.inflate(R.layout.fragment_step_detail, container, false) ;
        }
        ButterKnife.bind(this, rootView) ;
        mNextButton.setOnClickListener(v -> {
            //
            // Increment the current step value up to one less
            // than the number of steps
            //
            if ( mCurrentStep != (mRecipe.getSteps().size()-1)) {
                mCurrentStep++ ;
                UpdateUI(mRecipe.getSteps().get(mCurrentStep));
            }
        });
       // mPreviousButton = rootView.findViewById(R.id.step_previous);
        mPreviousButton.setOnClickListener(v -> {
            //
            // Decrement the current step if it is not zero
            //
            if (mCurrentStep != 0 ) {
                mCurrentStep--;
                UpdateUI(mRecipe.getSteps().get(mCurrentStep));
            }
        });
        //
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_CURRENT_STEP, mCurrentStep);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if ( savedInstanceState != null ) {
            mCurrentStep = savedInstanceState.getInt(RECIPE_CURRENT_STEP) ;
        }
        UpdateUI(mRecipe.getSteps().get(mCurrentStep)) ;
    }

    private void UpdateUI(Step step) {
        Activity activity = getActivity() ;
        activity.setTitle(step.getShort_description());
        mDescription_tv.setText(step.getDescription());
        if ( mCurrentStep == 0 ) {
            mPreviousButton.setVisibility(Button.INVISIBLE);
            mNextButton.setVisibility(Button.VISIBLE);
        } else if ( mCurrentStep == (mRecipe.getSteps().size()-1) ) {
            mPreviousButton.setVisibility(Button.VISIBLE);
            mNextButton.setVisibility(Button.INVISIBLE);
        } else {
            mPreviousButton.setVisibility(Button.VISIBLE);
            mNextButton.setVisibility(Button.VISIBLE);
        }
        //
        // If the player is playing ... stop it
        //
        if ( mExoPlayer != null ) {
            if ( mExoPlayer.getPlaybackState() != SimpleExoPlayer.STATE_IDLE) {
                mExoPlayer.stop();
            }
        }
        if ( !step.getVideo_url().equals("") ) {
            mPlayerView.setVisibility(View.VISIBLE);
            mPlayerNotAvailable.setVisibility(View.INVISIBLE);
            initializeVideoPlayer();
            setUriInPlayer(step) ;
        } else {
            mPlayerView.setVisibility(View.INVISIBLE);
            mPlayerNotAvailable.setVisibility(View.VISIBLE);
        }
    }

    private void initializeVideoPlayer() {
        if ( mExoPlayer == null ) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(),
                    new DefaultLoadControl());
            mPlayerView.setPlayer(mExoPlayer);
        }
    }

    private void setUriInPlayer(Step step) {
        Context context = getContext();

        String userAgent = Util.getUserAgent(context, "BakingTime");

        Uri videoUri = Uri.parse(step.getVideo_url());
        MediaSource mediaSource =
                new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(context, userAgent),
                        new DefaultExtractorsFactory(), null, null);
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.prepare(mediaSource);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
