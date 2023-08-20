package com.example.autosilentapp.database.local.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.autosilentapp.database.local.database.Database;
import com.example.autosilentapp.database.local.model.Profile;
import com.example.autosilentapp.database.local.database.ProfileDao;

import java.util.List;

public class ProfileRepository {

    private final ProfileDao profileDao;
    private final LiveData<List<Profile>> profilesLiveData;

    public ProfileRepository(Application application) {
        Database db = Database.getDatabase(application);
        profileDao = db.profileDao();
        profilesLiveData = profileDao.getAllProfiles();
    }
    public void insert(Profile profile) {
        Database.databaseWriteExecutor.execute(() -> {
            profileDao.insert(profile);
        });
    }

    public void update(Profile profile) {
        Database.databaseWriteExecutor.execute(() -> {
            profileDao.update(profile);
        });
    }

    public LiveData<List<Profile>> getProfileLiveData() {
        return profilesLiveData;
    }

    public void delete(int profileId){
        Database.databaseWriteExecutor.execute(() -> {
          profileDao.delete(profileId);
        });
    }
}
