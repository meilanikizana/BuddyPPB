package com.example.buddyppb.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JournalRepository {

    private final JournalDao mJournalDao;
    private final BuddyDao mBuddyDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JournalRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);

        mJournalDao = db.journalDao();
        mBuddyDao = db.buddyDao();

    }

    public LiveData<List<Journal>> getAllJournal() {
        return mJournalDao.getAllJournal();
    }

    public Journal getJournalById(int journalId) {
        return mJournalDao.getJournalById(journalId);
    }

    public LiveData<Integer> getNewestJournalId() {
        return mJournalDao.getNewestJournalId();
    }

    public void insertJournal(Journal journal) {
        executorService.execute(() -> mJournalDao.insertJournal(journal));
    }

    public void deleteJournal(Journal journal) {
        executorService.execute(() -> mJournalDao.deleteJournal(journal));
    }

    public void updateJournal(Journal journal) {
        executorService.execute(() -> mJournalDao.updateJournal(journal));
    }


    public void updateIsAnalyzed(int journalId, boolean isAnalyzed) {
        mJournalDao.updateIsAnalyzed(journalId, isAnalyzed);
    }

    public boolean isResultJournalSaved(Integer journalId) {
        return mBuddyDao.isResultJournalSaved(journalId);
    }

    public void insertResultJournal(ResultJournal resultJournal) {
        mBuddyDao.insertResultJournal(resultJournal);
    }

    public void deleteResultJournal(int id) {
        mBuddyDao.deleteResultJournal(id);
    }

    public ResultJournal getResultJournal(int journalId) {
        return mBuddyDao.getResultJournal(journalId);
    }

    public LiveData<List<JournalEntry>> getAllJournalsHistory() {
        return mBuddyDao.getAllJournalsHistory();
    }

    public void insertJournalHistory(JournalEntry journalEntry) {
        mBuddyDao.insertJournalHistory(journalEntry);
    }

    public void writeJournal(String date) {
        executorService.execute(() -> {
            LocalDate today = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            JournalStreak lastStreak = mBuddyDao.getLastStreak();

            int newStreak = 1;
            if (lastStreak != null) {
                LocalDate lastDate = LocalDate.parse(lastStreak.getDate(), DateTimeFormatter.ISO_DATE);
                long diff = ChronoUnit.DAYS.between(lastDate, today);

                if (diff == 1) newStreak = lastStreak.getCurrentStreak() + 1;
                else if (diff == 0) newStreak = lastStreak.getCurrentStreak();
                else newStreak = 1;
            }

            JournalStreak newEntry = new JournalStreak(date, true, newStreak);
            mBuddyDao.insertOrUpdateJournal(newEntry);

            Log.d("STREAK", "New streak saved: " + newStreak);
        });
    }

    public JournalStreak getStreakData(String date) {

        // --- Aman dari error format tanggal ---
        LocalDate convertedDate;
        try {
            convertedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        } catch (Exception e) {
            return new JournalStreak(date, false, 0);
        }

        JournalStreak lastStreak = mBuddyDao.getLastStreak();

        // Jika belum ada streak sama sekali
        if (lastStreak == null) {
            return new JournalStreak(date, false, 0);
        }

        LocalDate lastStreakDate;
        try {
            lastStreakDate = LocalDate.parse(lastStreak.getDate(), DateTimeFormatter.ISO_DATE);
        } catch (Exception e) {
            return new JournalStreak(date, false, 0);
        }

        long daysDifference = ChronoUnit.DAYS.between(lastStreakDate, convertedDate);

        // Cek apakah hari ini sudah punya streak
        JournalStreak todayStreak = mBuddyDao.getStreaksForDate(date);

        // Jika tidak ada data hari ini & jaraknya 1 hari → streak berlanjut
        if (todayStreak == null && daysDifference == 1L) {
            return new JournalStreak(
                    date,
                    true,
                    lastStreak.getCurrentStreak() + 1
            );
        }

        // Jika tidak ada streak untuk hari ini → mulai baru
        if (todayStreak == null) {
            return new JournalStreak(date, true, 1);
        }

        // Jika ada streak untuk tanggal ini → kembalikan
        return todayStreak;
    }


    public int getHighestStreak() {
        return mBuddyDao.getHighestStreak();
    }

    public long insertJournalReturnId(Journal journal) {
        return mJournalDao.insertJournalReturnId(journal); // method DAO Room harus return long
    }

}
