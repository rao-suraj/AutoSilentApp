package com.example.autosilentapp.presentation.create_session;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.autosilentapp.database.local.repository.SessionRepository;
import com.example.autosilentapp.database.local.model.Session;

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
