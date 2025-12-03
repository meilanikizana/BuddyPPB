package com.example.buddyppb;

import android.app.Application;
import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.buddyppb.data.JournalEntry;
import com.example.buddyppb.data.JournalRepository;
import com.example.buddyppb.data.Journal;
import com.example.buddyppb.data.JournalStreak;
import com.example.buddyppb.data.ResultJournal;
import com.example.buddyppb.helper.DateHelper;

import org.threeten.bp.LocalDate;

import java.util.List;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class JournalViewModel extends AndroidViewModel {
    private final ExecutorService executor;

    private final JournalRepository mJournalRepository;

    private final MutableLiveData<Uri> _imageUri = new MutableLiveData<>();
    public MutableLiveData<Uri> getImageUri() { return _imageUri; }

    private final MutableLiveData<Boolean> _saveResult = new MutableLiveData<>();
    public LiveData<Boolean> getSaveResult() { return _saveResult; }

    private final MutableLiveData<Journal> _detailJournal = new MutableLiveData<>();
    public LiveData<Journal> getDetailJournal() { return _detailJournal; }

    private final MutableLiveData<ResultJournal> _resultJournal = new MutableLiveData<>();
    public LiveData<ResultJournal> getResultJournalLiveData() { return _resultJournal; }

    private final MutableLiveData<Integer> _currentStreak = new MutableLiveData<>();
    public LiveData<Integer> getCurrentStreak() { return _currentStreak; }

    private final MutableLiveData<Integer> _highestStreak = new MutableLiveData<>();
    public LiveData<Integer> getHighestStreak() { return _highestStreak; }

    public JournalViewModel(@NonNull Application application) {
        super(application);
        mJournalRepository = new JournalRepository(application);
        executor = Executors.newSingleThreadExecutor();
    }

    public void setImageUri(Uri uri) {
        _imageUri.setValue(uri);
    }

    public LiveData<List<Journal>> getAllJournal() {
        return mJournalRepository.getAllJournal();
    }

    public void getJournalById(int journalId) {
        executor.execute(() -> {
            Journal journal = mJournalRepository.getJournalById(journalId);
            _detailJournal.postValue(journal);  // postValue karena dijalankan di background thread
        });
    }

    public LiveData<Integer> getNewestJournalId() {
        return mJournalRepository.getNewestJournalId();
    }

    public void insertJournal(Journal journal) {
        executor.execute(() -> {
            try {
                if (journal != null) {
                    mJournalRepository.insertJournal(journal);
                }
                _saveResult.postValue(true);
            } catch (Exception e) {
                _saveResult.postValue(false);
            }
        });
    }

    public void updateJournal(Journal journal) {
        executor.execute(() -> {
            try {
                if (journal != null) {
                    mJournalRepository.updateJournal(journal);
                }
                _saveResult.postValue(true);
            } catch (Exception e) {
                _saveResult.postValue(false);
            }
        });
    }

    public void analyzeStatusUpdate(int id, boolean isAnalyzed) {
        executor.execute(() -> mJournalRepository.updateIsAnalyzed(id, isAnalyzed));
    }

    public void deleteJournal(Journal journal) {
        executor.execute(() -> {
            mJournalRepository.deleteJournal(journal);
        });
    }

    public void saveResultJournal(ResultJournal resultJournal) {
        executor.execute(() -> {
            boolean isSaved = mJournalRepository.isResultJournalSaved(resultJournal.getJournalId());
            if (!isSaved) {
                mJournalRepository.insertResultJournal(resultJournal);
            }
        });
    }

    public void deleteResultJournal(int id) {
        executor.execute(() -> mJournalRepository.deleteResultJournal(id));
    }

    public void getResultJournal(int journalId) {
        executor.execute(() -> {
            ResultJournal resultJournal = mJournalRepository.getResultJournal(journalId);
            _resultJournal.postValue(resultJournal);
        });
    }

    public void addJournalHistory(String date, String title) {
        executor.execute(() -> mJournalRepository.insertJournalHistory(new JournalEntry(date, title)));
    }

    public void writeJournal(String initialDate) {
        executor.execute(() -> {
            long timestamp = Long.parseLong(initialDate);
            String date = DateHelper.convertTimestampToDate(timestamp);

            // LANGSUNG PAKAI date YANG SUDAH "yyyy-MM-dd"
            mJournalRepository.writeJournal(date);

            JournalStreak streak = mJournalRepository.getStreakData(date);
            _currentStreak.postValue(streak.getCurrentStreak());
            _highestStreak.postValue(mJournalRepository.getHighestStreak());
        });
    }

    public LiveData<List<JournalEntry>> getAllJournalsHistory() {
        return mJournalRepository.getAllJournalsHistory();
    }

    public void getStreakData() {
        executor.execute(() -> {
            String today = LocalDate.now().toString();  // pastikan import org.threeten.bp.LocalDate jika pakai ThreeTenABP
            JournalStreak streak = mJournalRepository.getStreakData(today);
            _currentStreak.postValue(streak.getCurrentStreak());
            _highestStreak.postValue(mJournalRepository.getHighestStreak());
        });
    }

    public void insertJournalWithCallback(Journal journal, OnJournalInserted callback) {
        executor.execute(() -> {
            try {
                long id = mJournalRepository.insertJournalReturnId(journal); // method baru di repository
                journal.setId((int) id);
                _saveResult.postValue(true);
                if (callback != null) {
                    callback.onInserted(journal);
                }
            } catch (Exception e) {
                _saveResult.postValue(false);
            }
        });
    }

    public interface OnJournalInserted {
        void onInserted(Journal journal);
    }


}