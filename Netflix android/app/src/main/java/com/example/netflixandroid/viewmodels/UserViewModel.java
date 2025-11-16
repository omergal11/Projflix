package com.example.netflixandroid.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.netflixandroid.PreferencesManager;
import com.example.netflixandroid.entitles.User;
import com.example.netflixandroid.repositories.UserRepository;

import java.io.File;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;  //repository to handle the data of the user
    private MutableLiveData<String> statusMessage;  //message to show the status of the operation
    private MutableLiveData<User> userDetails; //user details

    public UserViewModel(Application application) {
        super(application);
        statusMessage = new MutableLiveData<>();
        userDetails = new MutableLiveData<>();
        userRepository = new UserRepository(statusMessage,userDetails);
    }
    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public LiveData<User> getUserDetails() {
        return userDetails;
    }

    public void login(String email, String password) {
        userRepository.login(email, password);
    }

    public void createUser(User user, File profileImage) {
        userRepository.createUser(user,profileImage);
    }

    public void loadUserDetails() {
        String userId = PreferencesManager.getUserId();
        userRepository.loadUserDetails(userId);
    }
}