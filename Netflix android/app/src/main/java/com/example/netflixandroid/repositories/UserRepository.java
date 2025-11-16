package com.example.netflixandroid.repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;


import com.example.netflixandroid.NetflixApplication;
import com.example.netflixandroid.PreferencesManager;
import com.example.netflixandroid.api.UserAPI;
import com.example.netflixandroid.daos.UserDao;
import com.example.netflixandroid.entitles.AppDataBase;
import com.example.netflixandroid.entitles.User;

import java.io.File;


//class to handle the data of the user
public class UserRepository {
    private UserDao userDao;
    private UserAPI userAPI;
    private MutableLiveData<User> userDetails;

    public UserRepository(MutableLiveData<String> statusMessage,MutableLiveData<User> userDetails) {
        Context context = NetflixApplication.getInstance().getApplicationContext();
        AppDataBase db = AppDataBase.getInstance(context); //get the database instance
        userDao = db.userDao();
        userAPI = new UserAPI(userDao,statusMessage,userDetails);
        this.userDetails = userDetails;
    }

    public void createUser(User user, File profileImage) {
        userAPI.createUser(user,profileImage);
    }

    public void login(String email, String password) {
        userAPI.login(email, password);
    }
    public void loadUserDetails(String userId) {
        new Thread(() -> {
            User user = userDao.getUserById(userId);
            if (user != null) {
                userDetails.postValue(user);
            } else {
                userAPI.getUserById(userId);
            }
        }).start();
    }
    public String getAuthToken() {
        return PreferencesManager.getToken();
    }
}

