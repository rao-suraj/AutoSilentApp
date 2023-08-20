package com.example.autosilentapp.presentation.list_profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.autosilentapp.database.local.model.Profile;
import com.example.autosilentapp.database.local.repository.ProfileRepository;

import java.util.List;

public class ProfileListViewModel extends AndroidViewModel {


    private final ProfileRepository profileRepository;
    private final LiveData<List<Profile>> profileLiveData;


    public ProfileListViewModel(@NonNull Application application) {
        super(application);
        profileRepository =new ProfileRepository(application);
        profileLiveData=profileRepository.getProfileLiveData();
    }
    public void update(Profile profile) {
        profileRepository.update(profile);
    }

    public LiveData<List<Profile>> getProfileLiveData() {
        return profileLiveData;
    }

    public void delete(int profileId){
        profileRepository.delete(profileId);
    }
}
