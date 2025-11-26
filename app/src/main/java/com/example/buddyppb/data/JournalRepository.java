package com.example.buddyppb.data;

import android.app.Application;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JournalRepository {
    private JournalDao mJournalDao;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JournalRepository(Application application) {
        JournalRoomDatabase db = JournalRoomDatabase.getDatabase(application);
        mJournalDao = db.journalDao();
    }

    public LiveData<List<Journal>> getAllJournal() {
        return mJournalDao.getAllJournal();
    }

    public void insert(Journal journal) {
        executorService.execute(() -> mJournalDao.insert(journal));
    }

    public void delete(Journal journal) {
        executorService.execute(() -> mJournalDao.delete(journal));
    }

    public void update(Journal journal) {
        executorService.execute(() -> mJournalDao.update(journal));
    }

    public LiveData<Journal> getJournalById(String id) {
        return mJournalDao.getJournalById(id);
    }
}