package com.example.buddyppb.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
@Dao
public interface BuddyDao {

    // --- History ---
    @Insert
    void insertJournalHistory(JournalEntry entry);

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    LiveData<List<JournalEntry>> getAllJournalsHistory();


    // --- Streak ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateJournal(JournalStreak streak);

    @Query("SELECT * FROM journal_streak WHERE date = :date LIMIT 1")
    JournalStreak getStreaksForDate(String date);

    @Query("SELECT * FROM journal_streak ORDER BY date DESC LIMIT 1")
    JournalStreak getLastStreak();

    @Query("SELECT MAX(currentStreak) FROM journal_streak")
    int getHighestStreak();


    // --- Result Journal ---
    @Query("SELECT EXISTS(SELECT 1 FROM result_journal WHERE journal_id = :id)")
    boolean isResultJournalSaved(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertResultJournal(ResultJournal resultJournal);

    @Query("DELETE FROM result_journal WHERE journal_id = :id")
    void deleteResultJournal(int id);

    @Query("SELECT * FROM result_journal WHERE journal_id = :id")
    ResultJournal getResultJournal(int id);
}
