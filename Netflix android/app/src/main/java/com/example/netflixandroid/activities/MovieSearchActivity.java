package com.example.netflixandroid.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.netflix.R;
import com.example.netflixandroid.adapters.MovieAdapter;
import com.example.netflixandroid.entitles.Category;
import com.example.netflixandroid.entitles.Movie;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;


import com.example.netflixandroid.viewmodels.CategoryViewModel;
import com.example.netflixandroid.viewmodels.MovieViewModel;

public class MovieSearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinnerCategory;
    private RecyclerView movieRecyclerView;

    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);

        // Initialize views
        spinnerCategory = findViewById(R.id.spinnerCategory);
        movieRecyclerView = findViewById(R.id.recyclerViewMovies);

        // Set up RecyclerView
        movieRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        movieAdapter = new MovieAdapter(movieList, movie -> {
            Intent intent = new Intent(MovieSearchActivity.this, MovieDetailsActivity.class);
            intent.putExtra("movieId", movie.getMongoDbId());
            startActivity(intent);
        });
        movieRecyclerView.setAdapter(movieAdapter);

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovies().observe(this, movies -> {
            movieList.clear();
            movieList.addAll(movies);
            movieAdapter.notifyDataSetChanged();
        });
        movieViewModel.uploadMovies();


        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.uploadCategoriesFromLocal();

        // Observe categories live data
        categoryViewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                // Create an ArrayAdapter using the categories' names
                List<String> categoryNames = new ArrayList<>();
                categoryNames.add(getString(R.string.all_movies));
                for (Category category : categories) {
                    categoryNames.add(category.getName()); // assuming getName() is the method to get the category name
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(adapter);
            }
        });

        // Search movies by category
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                if (!selectedCategory.equals(getString(R.string.all_movies))) {
                    movieViewModel.searchMoviesByCategory(selectedCategory);
                }
                else {
                    movieViewModel.uploadMovies();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovies(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) {
                    searchMovies(newText);
                }
                return true;
            }
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void searchMovies(String query) {
        movieViewModel.searchMovies(query);
    }
}


