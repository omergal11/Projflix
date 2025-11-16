package com.example.netflixandroid.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.netflix.R;

//activity for the main page
public class MainActivity extends AppCompatActivity {

    private Button btnLogin, btnSignUp;

    private ImageButton nightModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create back button and tool bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nightModeButton = findViewById(R.id.nightModeButton);
        //update the night mode icon
        updateNightModeIcon();
        nightModeButton.setOnClickListener(v -> {
            toggleNightMode();
        });
        btnLogin = findViewById(R.id.btnGoLogin);
        btnSignUp = findViewById(R.id.btnGoSignUp);
        //go to login activity
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //go to sign up activity
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
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
    //toggle between night and day mode
    private void toggleNightMode() {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            //switch to day mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            //switch to night mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
}
