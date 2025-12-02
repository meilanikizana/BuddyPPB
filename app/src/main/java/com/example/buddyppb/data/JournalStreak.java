package com.example.buddyppb.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "journal_streak")
public class JournalStreak {

    @PrimaryKey
    @NonNull
    private String date;

    private boolean isJournaled;
    private int currentStreak;

    // Empty constructor for Room
    public JournalStreak() {}

    // Full constructor
    public JournalStreak(@NonNull String date, boolean isJournaled, int currentStreak) {
        this.date = date;
        this.isJournaled = isJournaled;
        this.currentStreak = currentStreak;
    }

    // === Getter & Setter ===
    @NonNull
    public String getDate() { return date; }

    public void setDate(@NonNull String date) { this.date = date; }

    public boolean isJournaled() { return isJournaled; }
    public void setJournaled(boolean journaled) { isJournaled = journaled; }

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
}
