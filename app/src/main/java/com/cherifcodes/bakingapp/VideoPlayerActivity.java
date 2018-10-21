package com.cherifcodes.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {

    /*public static final String EXO_PLAYER_ERROR_MSG = "Simple ExoPlayer error.";
    private static final String APP_NAME = "BakingApp";
    private SimpleExoPlayer mSimpleExoPlayer;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private ProgressBar mProgressBar;

    private TextView mStepDescription_tv;
    private String mCurrVideoUrl;
    private long mCurrPlayerPosition; // Used to restore the current state of the SimpleExoPlayer
*/  public static final String VIDEO_FRAGMENT_TAG = "recipe steps fragment";
    FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) return;

        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fmLyt_frament_video_player, videoPlayerFragment,
                VIDEO_FRAGMENT_TAG)
                //.addToBackStack("saveVideoFragment")
                .commit();

    }

}
