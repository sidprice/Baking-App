package com.sidprice.android.baking_app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sidprice.android.baking_app.R;
import com.sidprice.android.baking_app.model.Recipe;
import com.sidprice.android.baking_app.model.Step;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment {
    private static final String TAG = RecipeStepFragment.class.getSimpleName();
    private static final String RECIPE_CURRENT_STEP = "RecipeCurrentStep" ;
    public static final String  RECIPE_VIDEO_STATE = "RecipeVideoState" ;
    public static final String RECIPE_VIDEO_POSITION = "RecipeVideoPosition" ;
    public static final String  RECIPE_VIDEO_PLAY_WHEN_READY = "PlayWhenReady" ;

    private Context     mContext ;
    @BindView(R.id.step_description)        TextView    mDescription_tv ;
    @BindView(R.id.step_video_player)       SimpleExoPlayerView mPlayerView;
    @BindView(R.id.step_recipe_thumbnail)   ImageView   mRecipeThumbnail;
    @BindView(R.id.step_next)               Button      mNextButton ;
    @BindView(R.id.step_previous)           Button      mPreviousButton ;

    private SimpleExoPlayer     mExoPlayer ;
    private int         mCurrentStep ;
    private Recipe      mRecipe ;
    private boolean     mTwoPaneMode ;
    private boolean     mPlayerStateUnknown = true ;
    private int         mVideoPlayerState = SimpleExoPlayer.STATE_IDLE;
    private long        mVideoPlayerPosition = 0 ;
    private boolean     mPlayWhenReady = true;

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
            //
            // Fragment used in Activity so set up the up navigation
            //
            setHasOptionsMenu(true) ;
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar() ;
           actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this, rootView) ;
        mNextButton.setOnClickListener(v -> {
            //
            // Increment the current step value up to one less
            // than the number of steps
            //
            if ( mCurrentStep != (mRecipe.getSteps().size()-1)) {
                mCurrentStep++ ;
                mVideoPlayerPosition = 0 ;
                mVideoPlayerState = SimpleExoPlayer.STATE_IDLE ;
                mPlayWhenReady = true ;     // Will start any video in the next step
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
                mVideoPlayerPosition = 0 ;
                mVideoPlayerState = SimpleExoPlayer.STATE_IDLE ;
                mPlayWhenReady = true ;     // Will start any video in the previous step
                UpdateUI(mRecipe.getSteps().get(mCurrentStep));
            }
        });
        //
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( mExoPlayer != null ) { // Step has video ?
            mExoPlayer.stop() ;
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
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
        outState.putInt(RECIPE_VIDEO_STATE, mVideoPlayerState) ;
        outState.putBoolean(RECIPE_VIDEO_PLAY_WHEN_READY, mPlayWhenReady);
        if ( mExoPlayer != null ) {
            outState.putLong(RECIPE_VIDEO_POSITION, mExoPlayer.getCurrentPosition());
        } else {
            // outState.putInt(RECIPE_VIDEO_STATE, SimpleExoPlayer.STATE_IDLE) ;
            outState.putLong(RECIPE_VIDEO_POSITION, 0);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if ( savedInstanceState != null ) {
            mCurrentStep = savedInstanceState.getInt(RECIPE_CURRENT_STEP) ;
            mVideoPlayerPosition = savedInstanceState.getLong(RECIPE_VIDEO_POSITION) ;
            mVideoPlayerState = savedInstanceState.getInt(RECIPE_VIDEO_STATE) ;
            mPlayWhenReady = savedInstanceState.getBoolean(RECIPE_VIDEO_PLAY_WHEN_READY) ;
        }
        // UpdateUI(mRecipe.getSteps().get(mCurrentStep)) ;
    }


    @Override
    public void onResume() {
        super.onResume();
        UpdateUI(mRecipe.getSteps().get(mCurrentStep)) ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Activity    activity = getActivity() ;
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(activity);
                //
                // Need to add the current recipe to the Intent extras
                //
                upIntent.putExtra(Recipe.RECIPE_PARCEL_KEY, mRecipe) ;
                if (NavUtils.shouldUpRecreateTask(activity, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(mContext)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(activity, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);    }

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
            if ( mVideoPlayerState != SimpleExoPlayer.STATE_IDLE) {
                mExoPlayer.stop();
            }
        }
        if ( !step.getVideo_url().equals("") ) {
            mPlayerView.setVisibility(View.VISIBLE);
            mRecipeThumbnail.setVisibility(View.INVISIBLE);
            initializeVideoPlayer();
            setUriInPlayer(step) ;
        } else {
            mPlayerView.setVisibility(View.INVISIBLE);
            if ( !step.getThumbnail_url().equals("") ) {
                Picasso.get()
                        .load(step.getThumbnail_url())
                        .error(R.drawable.silverware_fork_knife)
                        .into(mRecipeThumbnail);
            } else {
                mRecipeThumbnail.setImageResource(R.drawable.cake_making);
            }
            mRecipeThumbnail.setVisibility(View.VISIBLE);
        }
    }

    private void initializeVideoPlayer() {
        if ( mExoPlayer == null ) {
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
            mExoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    mPlayWhenReady = playWhenReady ;
                    mVideoPlayerState = playbackState ;
                    Log.d(TAG, "onPlayerStateChanged: playWhenReady = " + playWhenReady);
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity() {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                }
            });
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
        mExoPlayer.seekTo(mVideoPlayerPosition);
       // if ( mVideoPlayerState != SimpleExoPlayer.STATE_IDLE || mPlayerStateUnknown ) {
            mPlayerStateUnknown = false ;
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
        // }
        mExoPlayer.prepare(mediaSource);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
