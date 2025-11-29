package com.example.buddyppb;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public class app extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        com.jakewharton.threetenabp.AndroidThreeTen.init(this);
        AndroidThreeTen.init(this);
    }
}
