package com.example.netflixandroid.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.netflix.R;

import com.example.netflixandroid.activities.LoginActivity;
import com.example.netflixandroid.entitles.User;
import com.example.netflixandroid.viewmodels.UserViewModel;

import java.io.File;

public class SignUpActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    private EditText emailEditText, passwordEditText,userNameText,confirmPasswordtext;
    private File selectedImageFile = null;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }

        // linking the views in the xml
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button signUpButton = findViewById(R.id.signUpButton);
        Button selectProfileImageButton = findViewById(R.id.selectProfileImageButton);
        userNameText = findViewById(R.id.usernameEditText);
        confirmPasswordtext = findViewById(R.id.confirmPasswordtext);

        // create ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // update status message from the ViewModel
        userViewModel.getStatusMessage().observe(this, status -> {
            if (status != null) {
                signUpButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, status, Toast.LENGTH_SHORT).show();
            }
        });
        selectProfileImageButton.setOnClickListener(v -> {
            openGallery(); // Open the dialog to choose an image
        });

        // sign-up button pressed
        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordtext.getText().toString();
            String username = userNameText.getText().toString();
            if (!password.equals(confirmPassword)){
                Toast.makeText(SignUpActivity.this, "password not match", Toast.LENGTH_SHORT).show();
            }
            else {
                User newUser = new User(email, password,username);
                signUpButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                userViewModel.createUser(newUser,selectedImageFile);
            }
        });

        // linking the button to the design and move to the login when pressed
        Button btnLogin = findViewById(R.id.btnGoLogin);
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Find the ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    private static final int PICK_IMAGE_REQUEST = 1;
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // converting URI to a File
            selectedImageFile = getFileFromUri(selectedImageUri);

            // show error message if picture have not been selected
            if (selectedImageFile != null) {
                // check uf the file is an image
                String mimeType = getContentResolver().getType(selectedImageUri);
                if (mimeType != null && mimeType.startsWith("image/")) {
                    Toast.makeText(this, "Image selected: " + selectedImageFile.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    // file is not an image
                    Toast.makeText(this, "Selected file is not an image", Toast.LENGTH_SHORT).show();
                    selectedImageFile = null;
                }
            } else {
                Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getFileFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            // create a File object from the path
            return new File(filePath);
        }
        return null;  // if path is missing
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // check for permission
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // if we have permission
                openGallery();
            } else {
                // if we don't have permission
                Toast.makeText(this, "Permission denied, cannot access files", Toast.LENGTH_SHORT).show();
            }
        }
    }
}