package com.example.netflixandroid.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.netflixandroid.entitles.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movie_table WHERE mongoDbId = :mongoDbId LIMIT 1")
    Movie getMovieByMongoDbId(String mongoDbId);

    @Query("SELECT * FROM movie_table")
    List<Movie> getAllMovies();
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertList(List<Movie> body);
    @Query("DELETE FROM movie_table")
    void clear();
    @Insert
    void insertAll(List<Movie> movies);

}