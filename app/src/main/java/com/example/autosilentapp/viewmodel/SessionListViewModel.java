package com.example.autosilentapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.autosilentapp.repository.SessionRepository;
import com.example.autosilentapp.database.Session;

import java.util.List;

public class SessionListViewModel extends AndroidViewModel {
    private final SessionRepository sessionRepository;
    private final LiveData<List<Session>> sessionLiveData;
    public SessionListViewModel(@NonNull Application application) {
        super(application);
        sessionRepository =new SessionRepository(application);
        sessionLiveData=sessionRepository.getSessionLiveData();
    }
    public void update(Session session) {
        sessionRepository.update(session);
    }

    public LiveData<List<Session>> getSessionLiveData() {
        return sessionLiveData;
    }

    public void delete(int sessionId){
        sessionRepository.delete(sessionId);
    }
}
