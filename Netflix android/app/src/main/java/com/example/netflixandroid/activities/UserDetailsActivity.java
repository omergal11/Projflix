package com.example.netflixandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.netflix.R;
import com.example.netflixandroid.NetflixApplication;
import com.example.netflixandroid.PreferencesManager;
import com.example.netflixandroid.entitles.User;
import com.example.netflixandroid.viewmodels.UserViewModel;

import android.view.View;
import android.widget.Button;

public class UserDetailsActivity extends AppCompatActivity {

    private UserViewModel userViewModel;  // Declare the ViewModel to hold user data
    private TextView userNameTextView, userEmailTextView, userRoleTextView;  // Declare TextViews for user info
    private ImageView userProfileImageView;  // Declare ImageView for profile picture

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);  // Set the layout for the activity

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);  // Initialize the ViewModel

        // Initialize the UI elements
        userNameTextView = findViewById(R.id.userNameTextView);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userRoleTextView = findViewById(R.id.userRoleTextView);
        userProfileImageView = findViewById(R.id.userProfileImageView);

        // Setup the Toolbar and enable the back button
        Toolbar toolbar = findViewById(R.id.userDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Enable the back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Load user details if they are not already available in the ViewModel
        if (userViewModel.getUserDetails().getValue() == null) {
            userViewModel.loadUserDetails();  // Fetch user details from a repository or API
        }

        // Observe changes in the user data and update UI
        userViewModel.getUserDetails().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.e("user", user.toString());  // Log the user data for debugging
                if (user != null) {
                    userNameTextView.setText(user.getUsername());  // Set username
                    userEmailTextView.setText(user.getEmail());  // Set email
                    userRoleTextView.setText(user.getRole());  // Set role

                    // Load and display the user's profile image using Glide (if available)
                    if (user.getProfilePicture() != null) {
                        String baseUrl = NetflixApplication.getBaseUrl();  // Get the base URL from the application
                        String imageUrl = baseUrl + user.getProfilePicture();  // Construct the full image URL
                        Glide.with(UserDetailsActivity.this)
                                .load(imageUrl)  // Load the image URL
                                .into(userProfileImageView);  // Set the image into the ImageView
                    }
                }
            }
        });

        // Set up the sign-out button
        Button btnSignOut = findViewById(R.id.signOutButton);
        btnSignOut.setOnClickListener(v -> {
            signOutUser();  // Sign the user out when clicked
        });
    }

    // Method to handle user sign-out (clear data and navigate to LoginActivity)
    private void signOutUser() {
        PreferencesManager.clearUserData();  // Clear user data (e.g., SharedPreferences)
        Intent intent = new Intent(UserDetailsActivity.this, LoginActivity.class);  // Create intent to go to login screen
        startActivity(intent);  // Start the LoginActivity
        finish();  // Finish the current activity (close UserDetailsActivity)
    }

    // Handle the back button in the Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();  // Go back to the previous screen
                return true;
            default:
                return super.onOptionsItemSelected(item);  // Handle other options
        }
    }
}

