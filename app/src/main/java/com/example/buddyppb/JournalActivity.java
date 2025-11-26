package com.example.buddyppb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.buddyppb.databinding.ActivityJournalBinding;

public class JournalActivity extends AppCompatActivity {

    private ActivityJournalBinding binding;
    private JournalAdapter journalAdapter;
    private JournalViewModel journalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // RecyclerView Setup
        journalAdapter = new JournalAdapter();
        binding.rvJournal.setLayoutManager(new LinearLayoutManager(this));
        binding.rvJournal.setAdapter(journalAdapter);

        // ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        journalViewModel = new ViewModelProvider(this, factory).get(JournalViewModel.class);

        journalViewModel.getAllJournal().observe(this, journalList -> {
            journalAdapter.setListJournal(journalList);
            binding.ivBuddyEmptyJournal.setVisibility(journalList.isEmpty() ? View.VISIBLE : View.GONE);
        });

        // FAB Action
        binding.fabNewJournal.setOnClickListener(v -> {
            startActivity(new Intent(this, AddJournalActivity.class));
        });
    }
}