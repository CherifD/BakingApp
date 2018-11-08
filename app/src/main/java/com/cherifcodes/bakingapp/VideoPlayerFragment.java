package com.cherifcodes.bakingapp;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


/**
 * A {@link Fragment} subclass for streaming videos.
 */
public class VideoPlayerFragment extends Fragment {

    public static final String EXO_PLAYER_ERROR_MSG = "Simple ExoPlayer error.";
    public static final String THUMBNAIL_LOAD_ERROR_MSG = "Thumbnail error loading error message";
    private static final String APP_NAME = "BakingApp";
    private SimpleExoPlayer mSimpleExoPlayer;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private ProgressBar mProgressBar;

    private TextView mStepDescription_tv;
    private ImageView mCake_imv;
    private View mFragmentLayoutView;
    private String mCurrVideoUrl, mCurrStepDescription, mCurrThumbnailImageUrl;
    private long mCurrPlayerPosition; // Used to restore the current state of the SimpleExoPlayer
    private boolean mPlayWhenReady;
    private int mCurrentWindow;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize the views for this fragment
        mFragmentLayoutView = inflater.inflate(R.layout.fragment_video_player, container, false);
        mSimpleExoPlayerView = mFragmentLayoutView.findViewById(R.id.spv_simplePlayerView);
        mProgressBar = mFragmentLayoutView.findViewById(R.id.progress_bar);
        mStepDescription_tv = mFragmentLayoutView.findViewById(R.id.tv_recipe_step_instruction);
        mCake_imv = mFragmentLayoutView.findViewById(R.id.imageView_cake);
        if (savedInstanceState != null) {
            mCurrVideoUrl = savedInstanceState.getString(IntentConstants.VIDEO_URL_KEY);
            mCurrStepDescription = savedInstanceState.getString(IntentConstants.STEP_DESCRIPTION_KEY);
            mStepDescription_tv.setText(mCurrStepDescription);
            mCurrPlayerPosition = savedInstanceState.getLong(IntentConstants.CURR_PLAYER_POSITION_KEY);
            mCurrThumbnailImageUrl = savedInstanceState.getString(IntentConstants.THUMBNAIL_IMAGE_URL_KEY);
            savedInstanceState.putBoolean(IntentConstants.CURR_PLAYER_STATE_KEY, mPlayWhenReady);
            savedInstanceState.putInt(IntentConstants.CURR_PLAYER_WINDOW_POSITION_KEY, mCurrentWindow);
        } else {
            // Get the video url from the bundle
            Bundle argumentBundle = getArguments();
            if (argumentBundle == null) {
                Log.e(VideoPlayerFragment.class.getSimpleName(), "Null Bundle");
                return null;
            }
            mCurrVideoUrl = argumentBundle.getString(IntentConstants.VIDEO_URL_KEY);
            mCurrStepDescription = argumentBundle.getString(IntentConstants.STEP_DESCRIPTION_KEY);
            mCurrThumbnailImageUrl = argumentBundle.getString(IntentConstants.THUMBNAIL_IMAGE_URL_KEY);

            mStepDescription_tv.setText(mCurrStepDescription);
        }
        return mFragmentLayoutView;
    }

    private void initializeExoplayer(Uri currVideoUri) {
        // Check if both the video and thumbnail url strings are empty or null
        if (TextUtils.isEmpty(mCurrVideoUrl) && TextUtils.isEmpty(mCurrThumbnailImageUrl)) {
            // Load the default image as the background image.
            mCake_imv.setImageResource(R.drawable.ic_cake_pink_24dp);
            hidePlayer();
        } else if (TextUtils.isEmpty(mCurrVideoUrl)) { // Invalid video url. Show thumbnail image
            // if it's valid and return.
            boolean isInvalidThumbnailUrlStr = mCurrThumbnailImageUrl.endsWith(".mp4") ||
                    TextUtils.isEmpty(mCurrThumbnailImageUrl);
            // Ensure that the thumbnail image url string is valid
            if (isInvalidThumbnailUrlStr) {
                // Load the default image as the background image.
                mCake_imv.setImageResource(R.drawable.ic_cake_pink_24dp);
            } else {
                // Possibly valid thumbnail url. Try to load the thumbnail image
                Picasso.with(getActivity())
                        .load(mCurrThumbnailImageUrl)
                        .error(R.drawable.ic_cake_pink_24dp)
                        .into(mCake_imv);
            }
            // Hide the progressbar and the Player
            hidePlayer();
        } else if (mSimpleExoPlayer == null) { // Video url string is non-empty at this point
            playVideo(currVideoUri);
            hideImage();
        } else {
            Log.i(VideoPlayerFragment.class.getSimpleName(), "Non null SimpleExoPlayer. " +
                    "No need to create a new one.");
        }
    }

    /**
     * Hide the progressbar and the PlayerView
     */
    private void hidePlayer() {
        mSimpleExoPlayerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void hideImage() {
        mCake_imv.setVisibility(View.INVISIBLE);
    }

    /**
     * Starts streaming the video located at the specified url
     *
     * @param currVideoUri uri where the video is located
     */
    private void playVideo(Uri currVideoUri) {
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

            // Connect MediaSource with SimpleExoPlayer seek to current position and play when ready.
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.seekTo(mCurrPlayerPosition);
            mSimpleExoPlayer.setPlayWhenReady(true);
            listenToExoPlayerEvents();
        } catch (Exception e) {
            Log.e(VideoPlayerActivity.class.getSimpleName(), EXO_PLAYER_ERROR_MSG);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSimpleExoPlayer != null) {
            mCurrPlayerPosition = mSimpleExoPlayer.getCurrentPosition();
        } else {
            mCurrPlayerPosition = 0;
        }
        outState.putLong(IntentConstants.CURR_PLAYER_POSITION_KEY, mCurrPlayerPosition);
        outState.putString(IntentConstants.VIDEO_URL_KEY, mCurrVideoUrl);
        outState.putString(IntentConstants.STEP_DESCRIPTION_KEY, mCurrStepDescription);
        outState.putString(IntentConstants.THUMBNAIL_IMAGE_URL_KEY, mCurrThumbnailImageUrl);
        outState.putBoolean(IntentConstants.CURR_PLAYER_STATE_KEY, mPlayWhenReady);
        outState.putInt(IntentConstants.CURR_PLAYER_WINDOW_POSITION_KEY, mCurrentWindow);
    }

    /**
     * Release SimpleExoPlayer.
     */
    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mCurrPlayerPosition = mSimpleExoPlayer.getCurrentPosition();
            mCurrentWindow = mSimpleExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
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
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializeExoplayer(Uri.parse(mCurrVideoUrl));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT < 23) {
            initializeExoplayer(Uri.parse(mCurrVideoUrl));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
