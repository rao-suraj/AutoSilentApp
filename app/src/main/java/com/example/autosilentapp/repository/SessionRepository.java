package com.example.autosilentapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.autosilentapp.database.Session;
import com.example.autosilentapp.database.SessionDao;
import com.example.autosilentapp.database.SessionDatabase;

import java.util.List;

public class SessionRepository {
    private final SessionDao sessionDao;
    private final LiveData<List<Session>> sessionLiveData;

    public SessionRepository(Application application) {
        SessionDatabase db = SessionDatabase.getDatabase(application);
        sessionDao = db.sessionDao();
        sessionLiveData = sessionDao.getAlarms();
    }
    public void insert(Session session) {
        SessionDatabase.databaseWriteExecutor.execute(() -> {
            sessionDao.insert(session);
        });
    }

    public void update(Session session) {
        SessionDatabase.databaseWriteExecutor.execute(() -> {
            sessionDao.update(session);
        });
    }

    public LiveData<List<Session>> getSessionLiveData() {
        return sessionLiveData;
    }

    public void delete(int sessionId){
        SessionDatabase.databaseWriteExecutor.execute(() -> {
            sessionDao.delete(sessionId);
        });
    }
}
