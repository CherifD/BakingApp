package com.cherifcodes.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class VideoPlayerActivity extends AppCompatActivity {

    public static final String VIDEO_FRAGMENT_TAG = "recipe steps fragment";
    FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) return;

        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        videoPlayerFragment.setArguments(getIntent().getExtras());
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fmLyt_frament_video_player, videoPlayerFragment,
                VIDEO_FRAGMENT_TAG)
                .commit();
    }
}
