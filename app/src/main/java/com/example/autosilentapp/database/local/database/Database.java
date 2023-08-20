package com.example.autosilentapp.database.local.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.autosilentapp.database.local.model.Profile;
import com.example.autosilentapp.database.local.model.Session;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {Session.class, Profile.class}, version = 3, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract SessionDao sessionDao();
    public abstract ProfileDao profileDao();
    private static volatile Database INSTANCE;
    private static final int NUMBER_OF_THREADS = 4 ;
    public static final ExecutorService databaseWriteExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            Database.class,
                            "session_database"
                    )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
