package com.example.autosilentapp.database.local.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.autosilentapp.database.local.model.Session;

import java.util.List;

@Dao
public interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Session session);

    @Query("DELETE FROM session_table")
    void deleteAll();

    @Query("SELECT * FROM session_table ORDER BY sessionId ASC")
    LiveData<List<Session>> getAlarms();

    @Update
    void update(Session session);

    @Query("Delete from session_table where sessionId = :sessionId")
    void delete(int sessionId);
}
