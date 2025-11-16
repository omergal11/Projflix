package com.example.netflixandroid.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.netflixandroid.entitles.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Category category);

    @Insert
    void insertAll(List<Category> categories);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM category_table")
    List<Category> getAllCategories();

    @Query("SELECT * FROM category_table WHERE mongoDbId = :categoryId LIMIT 1")
    Category getCategoryById(String categoryId);

    @Query("SELECT * FROM category_table WHERE name = :name LIMIT 1")
    Category getCategoryByName(String name);
    @Insert
    void insertList(List<Category> body);
    @Query("DELETE FROM category_table")
    void clear();
}
