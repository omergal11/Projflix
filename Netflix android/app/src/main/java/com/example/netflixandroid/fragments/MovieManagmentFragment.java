package com.example.netflixandroid.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflix.R;
import com.example.netflixandroid.adapters.MovieAdapter;
import com.example.netflixandroid.entitles.Movie;
import com.example.netflixandroid.viewmodels.MovieViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MovieManagmentFragment extends Fragment {
    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private FloatingActionButton fabAddMovie;

    private ProgressBar progressBar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    private static final int PICK_TRAILER_VIDEO_REQUEST = 3;

    private static final int PERMISSION_REQUEST_CODE = 1001;
    private File selectedImage = null;
    private File selectedVideoMovie = null;
    private File selectedVideoTrailer = null;

    public MovieManagmentFragment() {
        // Required empty public constructor
    }

    public static MovieManagmentFragment newInstance() {
        return new MovieManagmentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_managment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize the ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        //initialize the RecyclerView
        RecyclerView movieRecyclerView = view.findViewById(R.id.recyclerViewMovies);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        movieAdapter = new MovieAdapter(movieList, movie -> showMovieDialog(movie));
        movieRecyclerView.setAdapter(movieAdapter);

        //initialize the FAB
        fabAddMovie = view.findViewById(R.id.fabAddMovie);
        fabAddMovie.setOnClickListener(v -> showMovieDialog(null));
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //observe the movies list
        movieViewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
            fabAddMovie.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            movieList.clear();
            movieList.addAll(movies);
            movieAdapter.notifyDataSetChanged();
        });
        //observe the status message
        movieViewModel.getStatusMessage().observe(getViewLifecycleOwner(), status -> {
            if (status != null && !status.equals("upload search movies")) {
                fabAddMovie.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
            }
        });

        //search view
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovies(query);
                return true;
            }
            //if the user type more than 1 character
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 1) {
                    searchMovies(newText);
                }
                return true;
            }
        });

    }
    //search movies by name
    private void searchMovies(String query) {
        movieViewModel.searchMovies(query);
    }
    private void showMovieDialog(Movie movie) {
        //show the dialog with the movie details
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(movie == null ? getString(R.string.add_movie) : getString(R.string.edit_movie));

        //add the dialog layout
        View view = getLayoutInflater().inflate(R.layout.dialog_movie, null);
        EditText nameEditText = view.findViewById(R.id.editTextMovieName);
        EditText minutesEditText = view.findViewById(R.id.editTextMovieMinutes);
        EditText yearEditText = view.findViewById(R.id.editTextMovieYear);
        EditText descriptionEditText = view.findViewById(R.id.editTextMovieDescription);
        EditText castEditText = view.findViewById(R.id.editTextMovieCast);
        EditText directorEditText = view.findViewById(R.id.editTextMovieDirector);
        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        Button selectVideoTrailerButton = view.findViewById(R.id.selectVideoTrailerButton);
        Button selectVideoMovieButton = view.findViewById(R.id.selectVideoMovieButton);
        EditText categoryEditText = view.findViewById(R.id.editTextMovieCategories);

        //if have movie details
        if (movie != null) {
            nameEditText.setText(movie.getName());
            yearEditText.setText(String.valueOf(movie.getReleaseYear()));
            descriptionEditText.setText(movie.getDescription());
            directorEditText.setText(movie.getDirector());
            castEditText.setText(String.join(", ", movie.getCast()));
            minutesEditText.setText(movie.getMinutes());
            categoryEditText.setText(String.join(", ", movie.getCategories()));
        }
        //select image, movie and trailer
        selectImageButton.setOnClickListener(v -> openGalleryForImage());
        selectVideoMovieButton.setOnClickListener(v -> openGalleryForMovie());
        selectVideoTrailerButton.setOnClickListener(v -> openGalleryForTrailer());

        builder.setView(view);
        //add the buttons
        builder.setPositiveButton(movie == null ? "Add" : "Update", (dialog, which) -> {
            String name = nameEditText.getText().toString();
            String minutes = minutesEditText.getText().toString();
            int year;
            try { year = Integer.parseInt(yearEditText.getText().toString());}
            catch (Exception e){ year = -1; }
            String description = descriptionEditText.getText().toString();
            String categories = categoryEditText.getText().toString();
            String cast = castEditText.getText().toString();
            String director = directorEditText.getText().toString();

            //convert the categories to list
            List<String> categoryList = new ArrayList<>();
            if (!categories.trim().isEmpty()) {
                String[] categoryArray = categories.split(",");
                for (String category : categoryArray) {
                    categoryList.add(category.trim());
                }
            }

            //convert the cast to list
            List<String> castList = new ArrayList<>();
            if (!cast.trim().isEmpty()) {
                String[] castArray = cast.split(",");
                for (String actor : castArray) {
                    castList.add(actor.trim());
                }
            }

            //create the movie object
            Movie newMovie = new Movie(name, minutes , description, year, categoryList , castList, director);

            if (movie == null) {
                movieViewModel.createMovie(newMovie, selectedImage, selectedVideoMovie, selectedVideoTrailer);
            } else {
                movieViewModel.updateMovie(movie, name, minutes , description, year, categoryList , castList, director, selectedImage, selectedVideoMovie, selectedVideoTrailer);
            }
            fabAddMovie.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        });

        if (movie != null) {
            builder.setNegativeButton("Delete", (dialog, which) -> movieViewModel.deleteMovie(movie));
        } else {
            builder.setNegativeButton("Cancel", null);
        }

        builder.show();
    }
    //open the gallery to select an image
    private void openGalleryForImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    //open the gallery to select a movie
    private void openGalleryForMovie() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }
    //open the gallery to select a trailer
    private void openGalleryForTrailer() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_TRAILER_VIDEO_REQUEST);
    }
    //get the selected file
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri selectedUri = data.getData();
            File selectedFile = getFileFromUri(selectedUri);
            //check if the file is an imahe or a video
            if (selectedFile != null) {
                String mimeType = getContext().getContentResolver().getType(selectedUri);

                if (requestCode == PICK_IMAGE_REQUEST && mimeType != null && mimeType.startsWith("image/")) {
                    selectedImage = selectedFile;
                    Toast.makeText(getContext(), "Image selected: " + selectedImage.getName(), Toast.LENGTH_SHORT).show();
                } else if (requestCode == PICK_VIDEO_REQUEST && mimeType != null && mimeType.startsWith("video/")) {
                    selectedVideoMovie = selectedFile;
                    Toast.makeText(getContext(), "Movie video selected: " + selectedVideoMovie.getName(), Toast.LENGTH_SHORT).show();
                } else if (requestCode == PICK_TRAILER_VIDEO_REQUEST && mimeType != null && mimeType.startsWith("video/")) {
                    selectedVideoTrailer = selectedFile;
                    Toast.makeText(getContext(), "Trailer selected: " + selectedVideoTrailer.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Invalid file selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //check if the permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //check if the permission is granted
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                //permission denied
                Toast.makeText(getContext(), "Permission denied, cannot access files", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    //get the file from the uri
    private File getFileFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return new File(filePath);
        }
        return null;
    }
}