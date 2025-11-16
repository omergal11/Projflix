package com.example.netflixandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflixandroid.NetflixApplication;
import com.example.netflix.R;
import com.example.netflixandroid.adapters.MovieAdapter;
import com.example.netflixandroid.entitles.Movie;
import com.example.netflixandroid.entitles.StringListConverter;
import com.example.netflixandroid.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView movieTitleTextView, movieDetailsTextView, movieDirectorTextView,
            movieCastTextView, movieReleaseYearTextView,movieCategories;
    private ImageView movieImageView;
    private RecyclerView similarMoviesRecyclerView;
    private MovieAdapter similarMoviesAdapter;

    private List<Movie> similarMovies = new ArrayList<>();
    private MovieViewModel movieViewModel;
    private Button playButton;
    private ImageButton backButton;

    private ProgressBar progressBar;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        movieTitleTextView = findViewById(R.id.movieTitle);
        movieCategories = findViewById(R.id.movieCategories);
        movieDetailsTextView = findViewById(R.id.movieDetails);
        movieDirectorTextView = findViewById(R.id.movieDirector);
        movieCastTextView = findViewById(R.id.movieCast);
        movieReleaseYearTextView = findViewById(R.id.movieReleaseYear);
        movieImageView = findViewById(R.id.movieBanner);
        similarMoviesRecyclerView = findViewById(R.id.similarMoviesRecyclerView);
        playButton = findViewById(R.id.playButton);
        backButton = findViewById(R.id.backButton);
        progressBar = findViewById(R.id.progressBar);
        similarMovies = new ArrayList<>();
        similarMoviesRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);



        String movieId = getIntent().getStringExtra("movieId");


        movieViewModel.setFeaturedMovie(movieId);


        movieViewModel.getFeaturedMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie != null) {
                    updateMovieDetails(movie);
                    loadSimilarMovies(movie.getMongoDbId());
                }
            }
        });
        backButton.setOnClickListener(v -> {

            Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);  // MainActivity הוא מסך הבית
            startActivity(intent);
            finish();
        });
        similarMoviesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        similarMoviesAdapter = new MovieAdapter(similarMovies, movie -> {
            Intent intent = new Intent(MovieDetailsActivity.this, MovieDetailsActivity.class);
            intent.putExtra("movieId", movie.getMongoDbId());
            startActivity(intent);
        });
        similarMoviesRecyclerView.setAdapter(similarMoviesAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                movieViewModel.getMovies().observe(MovieDetailsActivity.this, new Observer<List<Movie>>() {
                    @Override
                    public void onChanged(List<Movie> movies) {
                        if (movies != null && !movies.isEmpty()) {
                            similarMovies.clear();
                            similarMovies.addAll(movies);
                            similarMoviesAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            similarMoviesRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            similarMoviesRecyclerView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }, 600);

        playButton.setOnClickListener(v -> {
            playMovie();
        });

    }

    private void updateMovieDetails(Movie movie) {

        this.movie = movie;
        movieTitleTextView.setText(movie.getName());
        movieDetailsTextView.setText(movie.getDescription());
        movieCategories.setText(getString(R.string.categories)+ ": " + StringListConverter.fromStringListWithComma(movie.getCategories()));
        movieDirectorTextView.setText(getString(R.string.director)+ ": " + movie.getDirector());
        movieCastTextView.setText(getString(R.string.cast)+ ": " +StringListConverter.fromStringListWithComma(movie.getCast()));
        movieReleaseYearTextView.setText(getString(R.string.realese_year)+ ": " + String.valueOf(movie.getReleaseYear()));

        String baseUrl = NetflixApplication.getBaseUrl();
        String imageUrl = baseUrl + movie.getMainImage();


        Glide.with(this)
                .load(imageUrl)
                .into(movieImageView);
    }

    private void loadSimilarMovies(String movieId) {
        movieViewModel.uploadRecommendMovies(movieId);
    }


    private void playMovie() {
        Toast.makeText(this, getString(R.string.playing_movie), Toast.LENGTH_SHORT).show();
        movieViewModel.watchThisMovie(movie.getMongoDbId());
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("movieFile", movie.getMovieFile());
        startActivity(intent);
    }
}
