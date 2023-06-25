package com.example.autosilentapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.autosilentapp.SessionRepository;
import com.example.autosilentapp.database.Session;

public class CreateSessionViewModel extends AndroidViewModel {
    private SessionRepository sessionRepository;
    public CreateSessionViewModel(@NonNull Application application) {
        super(application);
        sessionRepository=new SessionRepository(application);
    }
    public void insert(Session session) {
        sessionRepository.insert(session);
    }
    public void update(Session session) {
        sessionRepository.update(session);
    }
}
