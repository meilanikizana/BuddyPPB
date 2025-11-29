package com.example.buddyppb.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "journal_entries")
public class JournalEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String date;
    private String title;

    // Empty constructor (Room requirement)
    public JournalEntry() {}

    public JournalEntry(String date, String title) {
        this.date = date;
        this.title = title;
    }

    // === Getter & Setter ===
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
