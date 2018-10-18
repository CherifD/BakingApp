package com.cherifcodes.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;


/**
 * A {@link Fragment} subclass for streaming videos.
 */
public class VideoPlayerFragment extends Fragment {

    public static final String EXO_PLAYER_ERROR_MSG = "Simple ExoPlayer error.";
    private static final String APP_NAME = "BakingApp";
    private SimpleExoPlayer mSimpleExoPlayer;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private ProgressBar mProgressBar;

    private TextView mStepDescription_tv;
    private String mCurrVideoUrl;
    private long mCurrPlayerPosition; // Used to restore the current state of the SimpleExoPlayer

    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrPlayerPosition = savedInstanceState.getLong(IntentConstants.CURR_PLAYER_POSITION_KEY);
        }

        // Inflate the layout for this fragment
        View fragmentLayoutView = inflater.inflate(R.layout.fragment_video_player, container, false);


        mSimpleExoPlayerView = fragmentLayoutView.findViewById(R.id.spv_simplePlayerView);
        mProgressBar = fragmentLayoutView.findViewById(R.id.progress_bar);
        mStepDescription_tv = fragmentLayoutView.findViewById(R.id.tv_recipe_step_instruction);
        // Get the video url from the bundle
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            return fragmentLayoutView;
        }
        mCurrVideoUrl = bundle.getString(IntentConstants.VIDEO_URL_KEY);
        mStepDescription_tv.setText(bundle.getString(IntentConstants.STEP_DESCRIPTION_KEY));

        initializeExoplayer(Uri.parse(mCurrVideoUrl));

        return fragmentLayoutView;
    }


    private void initializeExoplayer(Uri currVideoUri) {

        if (mSimpleExoPlayer == null) {
            // Instantiate the SimpleExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector,
                    loadControl);

            // Connect SimpleExoPlayer with SimpleExoPlayerView
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

            try {
                //Create the datasource factory
                DefaultHttpDataSourceFactory defaultHttpDataSourceFactory =
                        new DefaultHttpDataSourceFactory(APP_NAME);
                //Create the extractor factory
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                //Create the MediaSource
                MediaSource mediaSource = new ExtractorMediaSource(currVideoUri,
                        defaultHttpDataSourceFactory, extractorsFactory, null, null);

                // Connect MediaSource with SimpleExoPlayer seekto current position and play when ready.
                mSimpleExoPlayer.prepare(mediaSource);
                mSimpleExoPlayer.seekTo(mCurrPlayerPosition);
                mSimpleExoPlayer.setPlayWhenReady(true);
                listenToExoPlayerEvents();
            } catch (Exception e) {
                Log.e(VideoPlayerActivity.class.getSimpleName(), EXO_PLAYER_ERROR_MSG);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        mCurrPlayerPosition = mSimpleExoPlayer.getCurrentPosition();
        outState.putLong(IntentConstants.CURR_PLAYER_POSITION_KEY, mCurrPlayerPosition);

    }

    /**
     * Release SimpleExoPlayer.
     */
    private void releasePlayer() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }

    private void listenToExoPlayerEvents() {
        mSimpleExoPlayer.addListener(new ExoPlayer.EventListener() {
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
                if (playbackState == ExoPlayer.STATE_BUFFERING) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

}
