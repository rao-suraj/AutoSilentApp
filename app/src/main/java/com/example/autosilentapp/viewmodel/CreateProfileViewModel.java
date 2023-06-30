package com.example.autosilentapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.autosilentapp.database.Profile;
import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.repository.ProfileRepository;
import com.example.autosilentapp.repository.SessionRepository;

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
