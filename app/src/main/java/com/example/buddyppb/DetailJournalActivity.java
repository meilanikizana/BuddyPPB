package com.example.buddyppb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.buddyppb.databinding.ActivityDetailJournalBinding;

public class DetailJournalActivity extends AppCompatActivity {

    private ActivityDetailJournalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Back button
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Edit (pindah ke WriteJournalActivity)
        binding.fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailJournalActivity.this, AddJournalActivity.class));
            }
        });

        // Ambil data dari Intent (pengganti SafeArgs)
        String title = getIntent().getStringExtra("title");
        if (title == null) title = "";
        String description = getIntent().getStringExtra("description");
        if (description == null) description = "";
        String imageUri = getIntent().getStringExtra("imageUri");
        if (imageUri == null) imageUri = "";

        binding.tvJournalTitle.setText(title);
        binding.tvJournalContent.setText(description);
        binding.ivJournalCover.setImageURI(Uri.parse(imageUri));
    }
}