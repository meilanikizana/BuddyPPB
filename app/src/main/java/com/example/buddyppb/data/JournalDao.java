package com.example.buddyppb.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface JournalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertJournal(Journal journal);

    @Update
    void updateJournal(Journal journal);

    @Delete
    void deleteJournal(Journal journal);

    @Query("SELECT * FROM journal ORDER BY id DESC")
    LiveData<List<Journal>> getAllJournal();

    @Query("SELECT * FROM journal WHERE id = :id")
    Journal getJournalById(int id);

    @Query("SELECT id FROM journal ORDER BY initialTimestamp DESC LIMIT 1")
    LiveData<Integer> getNewestJournalId();

    @Query("UPDATE journal SET isAnalyzed = :isAnalyzed WHERE id = :id")
    void updateIsAnalyzed(int id, boolean isAnalyzed);

    @Insert
    long insertJournalReturnId(Journal journal);

}
