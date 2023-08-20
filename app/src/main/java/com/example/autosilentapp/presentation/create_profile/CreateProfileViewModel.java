package com.example.autosilentapp.presentation.create_profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.autosilentapp.database.local.model.Profile;
import com.example.autosilentapp.database.local.repository.ProfileRepository;

public class CreateProfileViewModel extends AndroidViewModel {
    private final ProfileRepository profileRepository;
    public CreateProfileViewModel(Application application){
        super(application);
        profileRepository=new ProfileRepository(application);

    }
    public void insert(Profile profile) {
        profileRepository.insert(profile);
    }
    public void update(Profile profile) {
        profileRepository.update(profile);
    }
}
