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
    void insert(Journal journal);

    @Update
    void update(Journal journal);

    @Delete
    void delete(Journal journal);

    @Query("SELECT * from journal ORDER BY id ASC")
    LiveData<List<Journal>> getAllJournal();

    @Query("SELECT * from journal where id = :id")
    LiveData<Journal> getJournalById(String id);
}
