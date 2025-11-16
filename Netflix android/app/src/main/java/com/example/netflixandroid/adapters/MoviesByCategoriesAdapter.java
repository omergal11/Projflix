package com.example.netflixandroid.adapters;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.R;
import com.example.netflixandroid.entitles.Movie;

import java.util.ArrayList;
import java.util.List;

// Adapter for the movie list by categories
public class MoviesByCategoriesAdapter extends RecyclerView.Adapter<MoviesByCategoriesAdapter.CategoryViewHolder> {
    private List<Pair<String, List<Movie>>> categories = new ArrayList<>();
    private OnMovieClickListener onMovieClickListener;
    // Interface for the movie click listener
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
    // Constructor
    public void setOnMovieClickListener(OnMovieClickListener listener) {
        this.onMovieClickListener = listener;
    }
    // Create the view holder
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view, onMovieClickListener); 
    }
    // Bind the view holder
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Pair<String, List<Movie>> category = categories.get(position);
        holder.bind(category);
    }
    // Get the item count
    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }
    // Update the categories
    public void setCategories(List<Pair<String, List<Movie>>> categories) {
        this.categories = categories != null ? categories : new ArrayList<>();
        notifyDataSetChanged();
    }
    // View holder class
    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final RecyclerView moviesRecyclerView;
        private MovieAdapter movieAdapter;
        private OnMovieClickListener onMovieClickListener;
        // Constructor
        public CategoryViewHolder(@NonNull View itemView, OnMovieClickListener listener) {
            super(itemView);
            titleView = itemView.findViewById(R.id.categoryTitle);
            moviesRecyclerView = itemView.findViewById(R.id.moviesRecyclerView);
            // Set the on movie click listener
            this.onMovieClickListener = listener;

            if (moviesRecyclerView != null) {
                // Set up the recycler view
                movieAdapter = new MovieAdapter(new ArrayList<>(), movie -> {
                    if (onMovieClickListener != null) {
                        onMovieClickListener.onMovieClick(movie);
                    }
                });
                moviesRecyclerView.setLayoutManager(
                        new LinearLayoutManager(itemView.getContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false));
                moviesRecyclerView.setAdapter(movieAdapter);
            }
        }
        // Bind the view holder
        public void bind(Pair<String, List<Movie>> category) {
            if (titleView != null) {
                titleView.setText(category.first);
            }
            // Update the movie list
            if (movieAdapter != null) {
                movieAdapter = new MovieAdapter(category.second != null ? category.second : new ArrayList<>(), movie -> {
                    if (onMovieClickListener != null) {
                        onMovieClickListener.onMovieClick(movie);
                    }
                });
                moviesRecyclerView.setAdapter(movieAdapter);
            }
        }
    }
}
