package com.example.netflixandroid.entitles;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "user_table")
@TypeConverters(StringListConverter.class)
public class User {

    @PrimaryKey(autoGenerate = true)  // autoGenerate is for the internal id of Room
    private int id;  // Internal Room ID

    private String email;

    private int userId;

    private String password;

    private String profilePicture;
    private String username;

    private String role;

    // N:1 relationship with "com.example.netflix.SignUp.Movie" table (needs to be another entity for movies)
    private List<String> watchedMovies; // אחסן את הסרטים כ-String

    // Constructor
    public User(String email, String password,String username) {

        this.email = email;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<String> getWatchedMovies() {
        return watchedMovies;
    }
    public void setWatchedMovies(List<String> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", role='" + role + '\'' +
                ", watchedMovies='" + watchedMovies + '\'' +
                '}';
    }
}