package com.example.autosilentapp.database.local.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.autosilentapp.database.local.database.Database;
import com.example.autosilentapp.database.local.model.Session;
import com.example.autosilentapp.database.local.database.SessionDao;

import java.util.List;

public class SessionRepository {
    private final SessionDao sessionDao;
    private final LiveData<List<Session>> sessionLiveData;

    public SessionRepository(Application application) {
        Database db = Database.getDatabase(application);
        sessionDao = db.sessionDao();
        sessionLiveData = sessionDao.getAlarms();
    }
    public void insert(Session session) {
        Database.databaseWriteExecutor.execute(() -> {
            sessionDao.insert(session);
        });
    }

    public void update(Session session) {
        Database.databaseWriteExecutor.execute(() -> {
            sessionDao.update(session);
        });
    }

    public LiveData<List<Session>> getSessionLiveData() {
        return sessionLiveData;
    }

    public void delete(int sessionId){
        Database.databaseWriteExecutor.execute(() -> {
            sessionDao.delete(sessionId);
        });
    }
}
