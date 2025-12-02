package com.example.buddyppb;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buddyppb.data.datastore.UserModel;
import com.example.buddyppb.data.datastore.UserPreference;

public class LauncherActivity extends AppCompatActivity {

    private UserPreference userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userPreference = new UserPreference(this);

        UserModel user = userPreference.getUser();

        if (user.isFirstTime()) {
            // Pertama kali buka aplikasi → ke WelcomeActivity
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            // Sudah pernah buka → langsung ke MainActivity
            Intent intent = new Intent(this, JournalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
