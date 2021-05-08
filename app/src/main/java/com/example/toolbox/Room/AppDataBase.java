package com.example.toolbox.Room;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UserDao userDao();
    private static AppDataBase instance = null;

    public static synchronized AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "memo_Database")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }
}
