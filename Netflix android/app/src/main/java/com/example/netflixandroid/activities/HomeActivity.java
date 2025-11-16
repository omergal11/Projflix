package com.example.netflixandroid.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.netflix.R;
import com.example.netflixandroid.NetflixApplication;
import com.example.netflixandroid.PreferencesManager;
import com.example.netflixandroid.adapters.MoviesByCategoriesAdapter;
import com.example.netflixandroid.entitles.Movie;
import com.example.netflixandroid.viewmodels.CategoryViewModel;
import com.example.netflixandroid.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

//activity for the home page
public class HomeActivity extends AppCompatActivity {

    private MovieViewModel movieViewModel;
    private CategoryViewModel categoryViewModel;
    private RecyclerView moviesRecyclerView;
    private MoviesByCategoriesAdapter moviesAdapter;

    private Toolbar toolbar;

    private ImageButton nightModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Create back button and tool bar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); 


        nightModeButton = findViewById(R.id.nightModeButton);
        updateNightModeIcon();
        nightModeButton.setOnClickListener(v -> {
            toggleNightMode();
        });
        //init the recycler view
        moviesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        moviesAdapter = new MoviesByCategoriesAdapter();
        moviesRecyclerView.setAdapter(moviesAdapter);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.uploadCategories();
        categoryViewModel.getStatusMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if (status.equals("all categories upload")) {
                    movieViewModel.uploadPromotedMovies();
                }
            }
        });
        //set the on movie click listener
        moviesAdapter.setOnMovieClickListener(new MoviesByCategoriesAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                movieViewModel.uploadRecommendMovies(movie.getMongoDbId());
                movieViewModel.setFeaturedMovie(movie.getMongoDbId());;
                 Intent intent = new Intent(HomeActivity.this, MovieDetailsActivity.class);
                 intent.putExtra("movieId", movie.getMongoDbId());
                 startActivity(intent);
            }
        });

        ProgressBar progressBar = findViewById(R.id.progressBar);
        VideoView featuredMovieVideo = findViewById(R.id.featuredMovieVideo);
        TextView featuredMovieTitle = findViewById(R.id.featuredMovieTitle);
        Button playButton = findViewById(R.id.playButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageButton manageMoviesButton = toolbar.findViewById(R.id.manageMoviesButton);
        ImageButton personalAreaButton = toolbar.findViewById(R.id.personalAreaButton);
        ImageButton searchButton = toolbar.findViewById(R.id.searchButton);
        progressBar.setVisibility(View.VISIBLE);


        //config the recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        moviesRecyclerView.setLayoutManager(layoutManager);

        //init the view model
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        String userRole = PreferencesManager.getUserRole();
        if ("admin".equals(userRole)) {
            manageMoviesButton.setVisibility(View.VISIBLE);  //make the manage movies button visible if the user is admin
        } else {
            manageMoviesButton.setVisibility(View.GONE);  ///hide the manage movies button if the user is not admin
        }
        manageMoviesButton.setOnClickListener(v -> {
            //move to the manage movies activity
            startActivity(new Intent(HomeActivity.this, ManageActivity.class));
        });
        personalAreaButton.setOnClickListener(v -> {
            //move to the user details activity
            startActivity(new Intent(HomeActivity.this, UserDetailsActivity.class));
        });
        searchButton.setOnClickListener(v -> {
            //move to the search activity
            startActivity(new Intent(HomeActivity.this, MovieSearchActivity.class));
        });
        movieViewModel.getStatusMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if (status.equals("promoted movies upload") ) {
                    progressBar.setVisibility(View.GONE);
                    movieViewModel.getFeaturedMovie().observe(HomeActivity.this, new Observer<Movie>() {
                        @Override
                        public void onChanged(Movie movie) {
                            //display the featured movie
                            String baseUrl = NetflixApplication.getBaseUrl(); 
                            String videoTrailerUrl = baseUrl + movie.getTrailer();
                            featuredMovieTitle.setText(movie.getName());

                            //video view
                            Uri videoUri = Uri.parse(videoTrailerUrl);
                            featuredMovieVideo.setVideoURI(videoUri);
                            featuredMovieVideo.requestFocus();

                            //play the video automatically
                            featuredMovieVideo.start(); 

                            //play the movie when the play button is clicked
                            playButton.setOnClickListener(v -> {
                                movieViewModel.watchThisMovie(movie.getMongoDbId());
                                Intent intent = new Intent(HomeActivity.this, VideoPlayerActivity.class);
                                intent.putExtra("movieFile", movie.getMovieFile());
                                startActivity(intent);
                            });
                        }
                    });
                    //observe the promoted movies
                    movieViewModel.getPromotedMovies().observe(HomeActivity.this, new Observer<List<Pair<String, List<Movie>>>>() {
                        @Override
                        public void onChanged(List<Pair<String, List<Movie>>> categories) {
                            if (categories != null && !categories.isEmpty()) {
                                moviesAdapter.setCategories(categories);
                            } else {
                                Log.d("HomeActivity", "No categories available!");
                            }
                        }
                    });
                }
            }
        });
    }
    //update the night mode icon
    private void updateNightModeIcon() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            nightModeButton.setImageResource(R.drawable.baseline_lightbulb_24);
        } else {
            nightModeButton.setImageResource(R.drawable.outline_mode_night_24);
        }
    }
    //toggle the night mode
    private void toggleNightMode() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}

