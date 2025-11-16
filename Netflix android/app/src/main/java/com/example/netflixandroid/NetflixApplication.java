package com.example.netflixandroid;
import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.example.netflix.R;
import com.example.netflixandroid.entitles.AppDataBase;

//class to handle the application
public class NetflixApplication extends Application {
    private static NetflixApplication instance;
    private static String baseUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.init(this);
        baseUrl ="http://" + PreferencesManager.getIpAddress() + ":" + PreferencesManager.getPort() +"/";
        Log.d("baseurl",baseUrl);
        deleteDatabase("app_database");
        AppDataBase db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "app_database")  //create the database
                .fallbackToDestructiveMigration()
                .build();
        PreferencesManager.init(this);
        instance = this;
    }
    public static NetflixApplication getInstance() {
        return instance;
    }

    public static String getBaseUrl() { return baseUrl;}
}