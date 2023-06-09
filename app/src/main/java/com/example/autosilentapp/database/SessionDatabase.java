package com.example.autosilentapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Session.class, Profile.class}, version = 3, exportSchema = false)
public abstract class SessionDatabase extends RoomDatabase {
    public abstract SessionDao sessionDao();
    public abstract ProfileDao profileDao();
    private static volatile SessionDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4 ;
    public static final ExecutorService databaseWriteExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static SessionDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SessionDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            SessionDatabase.class,
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
