package com.example.buddyppb;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.buddyppb.data.JournalRepository;
import com.example.buddyppb.data.Journal;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JournalViewModel extends ViewModel {
    private final JournalRepository mJournalRepository;
    private final MutableLiveData<Uri> _imageUri = new MutableLiveData<>();
    public MutableLiveData<Uri> getImageUri() {
        return _imageUri;
    }

    private final MutableLiveData<Boolean> _saveResult = new MutableLiveData<>();
    public LiveData<Boolean> getSaveResult() {
        return _saveResult;
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JournalViewModel(Application application) {
        mJournalRepository = new JournalRepository(application);
    }

    public void setImageUri(Uri uri) {
        _imageUri.setValue(uri);
    }

    public LiveData<List<Journal>> getAllJournal() {
        return mJournalRepository.getAllJournal();
    }

    public LiveData<Journal> getJournalById(String id) {
        return mJournalRepository.getJournalById(id);
    }

    public void insert(final Journal journal) {
        executorService.execute(() -> {
            try {
                mJournalRepository.insert(journal);
                _saveResult.postValue(true); // Notify save was successful
            } catch (Exception e) {
                _saveResult.postValue(false); // Notify save failed
            }
        });
    }

    // For suspend functions, you can use similar executor approach or handle with coroutines in Kotlin only.
    public void update(final Journal journal) {
        executorService.execute(() -> {
            mJournalRepository.update(journal);
        });
    }

    public void delete(final Journal journal) {
        executorService.execute(() -> {
            mJournalRepository.delete(journal);
        });
    }
}