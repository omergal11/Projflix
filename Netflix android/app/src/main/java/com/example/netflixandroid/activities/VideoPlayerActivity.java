package com.example.netflixandroid.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netflixandroid.NetflixApplication;
import com.example.netflix.R;

import android.view.View;
import android.widget.ImageButton;

import android.widget.ProgressBar;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private ProgressBar progressBar;
    private ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

       //find the views
        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        closeButton = findViewById(R.id.closeButton);

        //view the progress bar
        progressBar.setVisibility(View.VISIBLE);

        //get the movie url
        String movieUrl = getIntent().getStringExtra("movieFile");
        String videoUrl = NetflixApplication.getBaseUrl() + movieUrl;

        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);

        //add media controller to the video view
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        //when the video is ready hide the progress bar
        videoView.setOnPreparedListener(mp -> {
            progressBar.setVisibility(View.GONE);
        });

        //start the video
        videoView.start();

        //close the video player
        closeButton.setOnClickListener(v -> onBackPressed());
    }
}
