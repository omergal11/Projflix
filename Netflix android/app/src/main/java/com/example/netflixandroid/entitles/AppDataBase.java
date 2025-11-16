package com.example.netflixandroid.entitles;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.netflixandroid.daos.CategoryDao;
import com.example.netflixandroid.daos.MovieDao;
import com.example.netflixandroid.daos.UserDao;

// Database class for the app
@Database(entities = {User.class, Category.class, Movie.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {

    private static volatile AppDataBase instance;

    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract MovieDao movieDao();
    // Migration from version 1 to version 2
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE user ADD COLUMN new_column TEXT");
        }
    };
    // Get the database instance
    public static synchronized AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "app_database")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }
}
