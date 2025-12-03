package com.example.buddyppb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.buddyppb.data.datastore.UserModel;
import com.example.buddyppb.data.datastore.UserPreference;
import com.example.buddyppb.databinding.ActivityJournalBinding;

public class JournalActivity extends AppCompatActivity {

    private ActivityJournalBinding binding;
    private JournalAdapter journalAdapter;
    private JournalViewModel journalViewModel;
    private UserPreference userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userPreference = new UserPreference(this);
        UserModel user = userPreference.getUser();

        binding.userName.setText(user.getName());

        // RecyclerView Setup
        journalAdapter = new JournalAdapter();
        binding.rvJournal.setLayoutManager(new LinearLayoutManager(this));
        binding.rvJournal.setAdapter(journalAdapter);

        // Klik item → Detail
        journalAdapter.setOnItemClickCallback(journal -> {
            Intent intent = new Intent(this, DetailJournalActivity.class);
            intent.putExtra("journal_id", journal.getId());
            startActivity(intent);
        });

        // ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        journalViewModel = new ViewModelProvider(this, factory).get(JournalViewModel.class);

        journalViewModel.getAllJournal().observe(this, journalList -> {
            journalAdapter.setListJournal(journalList);
            binding.ivBuddyEmptyJournal.setVisibility(journalList.isEmpty() ? View.VISIBLE : View.GONE);
        });

        // Button mulai jurnal
        binding.journalCard.setOnClickListener(v ->
                startActivity(new Intent(this, AddJournalActivity.class))
        );

        binding.btnStartJournal.setOnClickListener(v ->
                startActivity(new Intent(this, AddJournalActivity.class))
        );

        // Klik streak → History
        binding.streakJurnal.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryJournalActivity.class))
        );

        binding.streakJurnalCount.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryJournalActivity.class))
        );

        setupIcons();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Refresh streak
        journalViewModel.getStreakData();

        journalViewModel.getCurrentStreak().observe(this, currentStreak ->
                binding.streakJurnalCount.setText(String.valueOf(currentStreak))
        );
    }

    private void setupIcons() {

        binding.happyIcon.setOnClickListener(v ->
                Toast.makeText(this, "Senang mendengarnya", Toast.LENGTH_SHORT).show()
        );

        binding.normalIcon.setOnClickListener(v ->
                Toast.makeText(this, "Coba lakukan aktivitas baru yuk!", Toast.LENGTH_SHORT).show()
        );

        binding.anxiousIcon.setOnClickListener(v ->
                Toast.makeText(this, "Ada apa? yuk tulis jurnal hari ini!", Toast.LENGTH_SHORT).show()
        );

        binding.sadIcon.setOnClickListener(v ->
                Toast.makeText(this, "Percayalah semua akan baik-baik saja", Toast.LENGTH_SHORT).show()
        );
    }
}
