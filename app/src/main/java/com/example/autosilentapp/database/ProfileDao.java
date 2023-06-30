package com.example.autosilentapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Profile profile);

    @Query("DELETE FROM profile_table")
    void deleteAll();

    @Query("SELECT * FROM profile_table ORDER BY profileId ASC")
    LiveData<List<Profile>> getAllProfiles();

    @Update
    void update(Profile profile);

    @Query("DELETE FROM profile_table WHERE profileId = :profileId")
    void delete(int profileId);
}
