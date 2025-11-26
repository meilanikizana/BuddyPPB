package com.example.buddyppb.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Journal.class}, version = 1, exportSchema = false)
public abstract class JournalRoomDatabase extends RoomDatabase {
    public abstract JournalDao journalDao();

    private static volatile JournalRoomDatabase INSTANCE;

    public static JournalRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (JournalRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    JournalRoomDatabase.class, "history_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}