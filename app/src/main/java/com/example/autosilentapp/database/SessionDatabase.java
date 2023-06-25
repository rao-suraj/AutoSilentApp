package com.example.autosilentapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Session.class}, version = 1, exportSchema = false)
public abstract class SessionDatabase extends RoomDatabase {
    public abstract SessionDao sessionDao();
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
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
