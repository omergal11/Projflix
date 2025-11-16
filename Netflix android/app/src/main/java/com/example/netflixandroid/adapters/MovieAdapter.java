package com.example.netflixandroid.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.netflix.R;
import com.example.netflixandroid.NetflixApplication;
import com.example.netflixandroid.entitles.Movie;

import java.util.List;

// Adapter for the movie list
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private MovieClickListener movieClickListener;

    // Constructor
    public MovieAdapter(List<Movie> movies, MovieClickListener movieClickListener) {
        this.movies = movies;
        this.movieClickListener = movieClickListener;
    }
    // Create the view holder
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }
    // Bind the view holder
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }
    // Get the item count
    @Override
    public int getItemCount() {
        return movies.size();
    }
    // Update the movie list
    public void updateMovies(List<Movie> movies) {
        if (movies != null) {
            this.movies = movies;
            notifyDataSetChanged();
        }
    }
    // View holder class
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private ImageView movieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.movieTitle);
            movieImageView = itemView.findViewById(R.id.movieImage);
        }
        // Bind the movie
        public void bind(Movie movie) {
            nameTextView.setText(movie.getName());
            String baseUrl = NetflixApplication.getBaseUrl();
            String imageUrl = baseUrl + movie.getMainImage();
            Log.e("url",imageUrl);
            // Load the image
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .into(movieImageView);
            itemView.setOnClickListener(v -> {
                movieClickListener.onMovieClick(movie);
            });
            // Set the click listeners
            movieImageView.setOnClickListener(v -> {
                movieClickListener.onMovieClick(movie);
            });
            // Set the click listeners
            nameTextView.setOnClickListener(v -> {
                movieClickListener.onMovieClick(movie);
            });
        }
    }
    // Movie click listener interface
    public interface MovieClickListener {
        void onMovieClick(Movie movie);
    }
}

