package com.example.buddyppb;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.buddyppb.databinding.ActivityHistoryJournalBinding;
import com.example.buddyppb.helper.ViewUtils;

public class HistoryJournalActivity extends AppCompatActivity {

    private ActivityHistoryJournalBinding binding;
    private JournalViewModel journalViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHistoryJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewUtils.setupView(getWindow(), getSupportActionBar());

        // ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        journalViewModel = new ViewModelProvider(this, factory).get(JournalViewModel.class);


        // RecyclerView
        binding.rvJournalHistory.setLayoutManager(new LinearLayoutManager(this));

        // Observe journals history
        journalViewModel.getAllJournalsHistory().observe(this, history -> {

            HistoryJournalAdapter adapter = new HistoryJournalAdapter(history);
            binding.rvJournalHistory.setAdapter(adapter);

            if (history.isEmpty()) {
                binding.ivBuddyEmptyJournal.setVisibility(View.VISIBLE);
                binding.btnWriteJournal.setVisibility(View.VISIBLE);
            } else {
                binding.ivBuddyEmptyJournal.setVisibility(View.GONE);
                binding.btnWriteJournal.setVisibility(View.GONE);
            }
        });

        // Load streak data
        journalViewModel.getStreakData();

        // Observe current streak
        journalViewModel.getCurrentStreak().observe(this, currentStreak -> {
            binding.tvCurrentStreak.setText(String.valueOf(currentStreak));
        });

        // Observe highest streak
        journalViewModel.getHighestStreak().observe(this, highestStreak -> {
            binding.tvHighestStreak.setText(String.valueOf(highestStreak));
        });

        // Buttons
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnWriteJournal.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryJournalActivity.this, AddJournalActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
