package com.cherifcodes.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

public class VideoPlayerActivity extends AppCompatActivity {

    public static final String EXO_PLAYER_ERROR_MSG = "Simple ExoPlayer error.";
    private static final String APP_NAME = "BakingApp";
    private SimpleExoPlayer mSimpleExoPlayer;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private ProgressBar mProgressBar;

    private String mCurrVideoUrl;
    private long mCurrPlayerPosition; // Used to restore the current state of the SimpleExoPlayer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrPlayerPosition = savedInstanceState.getLong(IntentConstants.CURR_PLAYER_POSITION_KEY);
        }

        setContentView(R.layout.activity_video_player);
        mSimpleExoPlayerView = findViewById(R.id.spv_simplePplayerView);
        mProgressBar = findViewById(R.id.progress_bar);
        // Get the video url from the bundle
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        mCurrVideoUrl = bundle.getString(IntentConstants.VIDEO_URL_KEY);

        initializeExoplayer(Uri.parse(mCurrVideoUrl));
    }

    private void initializeExoplayer(Uri currVideoUri) {

        if (mSimpleExoPlayer == null) {
            // Instantiate the SimpleExoPlayer
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

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
    protected void onSaveInstanceState(Bundle outState) {
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
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
