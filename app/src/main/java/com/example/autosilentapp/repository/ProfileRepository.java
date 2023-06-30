package com.example.autosilentapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.autosilentapp.database.Profile;
import com.example.autosilentapp.database.ProfileDao;
import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.database.SessionDao;
import com.example.autosilentapp.database.SessionDatabase;

import java.util.List;

public class ProfileRepository {

    private final ProfileDao profileDao;
    private final LiveData<List<Profile>> profilesLiveData;

    public ProfileRepository(Application application) {
        SessionDatabase db = SessionDatabase.getDatabase(application);
        profileDao = db.profileDao();
        profilesLiveData = profileDao.getAllProfiles();
    }
    public void insert(Profile profile) {
        SessionDatabase.databaseWriteExecutor.execute(() -> {
            profileDao.insert(profile);
        });
    }

    public void update(Profile profile) {
        SessionDatabase.databaseWriteExecutor.execute(() -> {
            profileDao.update(profile);
        });
    }

    public LiveData<List<Profile>> getProfileLiveData() {
        return profilesLiveData;
    }

    public void delete(int profileId){
        SessionDatabase.databaseWriteExecutor.execute(() -> {
          profileDao.delete(profileId);
        });
    }
}
