package com.example.buddyppb;

import static com.example.buddyppb.DetailJournalActivity.EXTRA_JOURNAL;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.lifecycle.Observer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.buddyppb.data.Journal;
import com.example.buddyppb.databinding.ActivityAddJournalBinding;
import com.example.buddyppb.helper.DateHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class AddJournalActivity extends AppCompatActivity {

    private ActivityAddJournalBinding binding;
    private JournalViewModel journalViewModel;
    private Uri currentImageUri = null;
    private Boolean isUpdate = false;
    private Boolean isAnalyzed = false;
    private Journal journal = null;

    private ActivityResultLauncher<PickVisualMediaRequest> launcherGallery;
    private ActivityResultLauncher<Intent> uCropLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ===== Initialize uCropLauncher =====
        uCropLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri resultUri = UCrop.getOutput(result.getData());
                        if (resultUri != null) {
                            currentImageUri = resultUri;
                            showImage();
                        }
                    } else if (result.getResultCode() == UCrop.RESULT_ERROR) {
                        Throwable cropError = UCrop.getError(result.getData());
                        Log.e("uCrop", "Crop error: " + cropError);
                        showToast(getString(R.string.crop_error));
                    }
                }
        );

        // ===== Initialize launcherGallery =====
        launcherGallery = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        Uri destinationUri = Uri.fromFile(
                                new File(
                                        getCacheDir(),
                                        "cropped_image_" + System.currentTimeMillis() + ".jpg"
                                )
                        );
                        uCropLauncher.launch(
                                UCrop.of(uri, destinationUri).getIntent(this)
                        );
                    } else {
                        Log.d("Photo Picker", "No Media Selected");
                    }
                }
        );

        // ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        journalViewModel = new ViewModelProvider(this, factory).get(JournalViewModel.class);

        journal = getIntent().getParcelableExtra(EXTRA_JOURNAL);
        if (journal != null) {
            isUpdate = true;
            binding.etJournalTitle.setText(journal.getTitle());
            binding.etJournalContent.setText(journal.getDescription());
            currentImageUri = Uri.parse(journal.getImage());
            isAnalyzed = journal.isAnalyzed();
            showImage();
        } else {
            journal = new Journal();
        }

        // Title color opacity logic
        binding.etJournalTitle.setTextColor(ContextCompat.getColor(this, R.color.blue_70));
        binding.etJournalTitle.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int color = (s == null || s.length() == 0) ? R.color.blue_70 : R.color.blue;
                binding.etJournalTitle.setTextColor(ContextCompat.getColor(AddJournalActivity.this, color));
            }
        });

        // Back button
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        // Observe Image URI
        journalViewModel.getImageUri().observe(this, uri -> {
            if (uri != null) {
                currentImageUri = uri;
                binding.ivJournalCover.setImageURI(uri);
            }
        });

        // Button listeners
        binding.btnOpenGallery.setOnClickListener(v -> startGallery());
        binding.btnSave.setOnClickListener(v -> saveJournal());

        // Observe save result
        journalViewModel.getSaveResult().observe(this, isSuccess -> {
            if (isSuccess) {
                navigateToDetailJournalPage();
            } else {
                Toast.makeText(this, "Failed to save journal.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void navigateToDetailJournalPage() {
        journalViewModel.getNewestJournalId().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer journalId) {
                String title = binding.etJournalTitle.getText().toString();
                String description = binding.etJournalContent.getText().toString();
                String imageUri = currentImageUri.toString();
                long initialTimestamp = System.currentTimeMillis();
                String timestamp = "";
                boolean isAnalyzed = false;
                Journal journal = new Journal(
                        journalId,
                        title,
                        description,
                        imageUri,
                        initialTimestamp,
                        timestamp,
                        isAnalyzed
                );

                showLoading(false);
                Intent intent = new Intent(AddJournalActivity.this, DetailJournalActivity.class);
                intent.putExtra(DetailJournalActivity.EXTRA_JOURNAL, journal);
                startActivity(intent);
                finish();

                journalViewModel.writeJournal(Long.toString(initialTimestamp));
            }
        });
    }

    private void startGallery() {
        launcherGallery.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()
        );
    }

    private void showImage() {
        if (currentImageUri != null) {
            binding.ivJournalCover.setImageURI(currentImageUri);
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void saveJournal() {
        String title = binding.etJournalTitle.getText().toString();
        String image = null;
        if (currentImageUri != null) {
            image = currentImageUri.toString();
        }
        String description = binding.etJournalContent.getText().toString();
        Long initialTimestamp;
        if (isUpdate) {
            initialTimestamp = journal != null ? journal.getInitialTimestamp() : null;
        } else {
            initialTimestamp = System.currentTimeMillis();
        }
        String timestamp;
        if (isUpdate) {
            timestamp = "Diperbarui pada " + DateHelper.getCurrentDate();
        } else {
            timestamp = DateHelper.getCurrentDate();
        }

        if (title.isEmpty()) {
            binding.etJournalTitle.setError(getString(R.string.empty_journal_title));
        } else if (description.isEmpty()) {
            binding.etJournalContent.setError(getString(R.string.empty_journal_description));
        } else if (image == null) {
            showToast(getString(R.string.empty_journal_image));
        } else {
            showLoading(true);
            if (isUpdate) {
                Journal updatedJournal = null;
                if (journal != null) {
                    updatedJournal = new Journal(
                            journal.getId(),
                            title,
                            image,
                            description,
                            initialTimestamp,
                            timestamp,
                            journal.isAnalyzed()
                    );
                }
                journalViewModel.updateJournal(updatedJournal);
                if (updatedJournal != null && updatedJournal.isAnalyzed()) {
                    journalViewModel.deleteResultJournal(updatedJournal.getId());
                    journalViewModel.analyzeStatusUpdate(updatedJournal.getId(), false);
                }
                showToast(getString(R.string.journal_updated));
            } else {
                Journal newJournal = null;
                if (journal != null) {
                    newJournal = new Journal(
                            0,
                            title,
                            image,
                            description,
                            initialTimestamp,
                            timestamp,
                            false
                    );
                }
                journalViewModel.insertJournal(newJournal);
                journalViewModel.addJournalHistory(
                        newJournal != null ? newJournal.getTimestamp() : null,
                        newJournal != null ? newJournal.getTitle() : null
                );
                showToast(getString(R.string.journal_saved));
            }
        }
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressIndicator.setVisibility(View.VISIBLE);
            binding.progressIndicator.show();
        } else {
            binding.progressIndicator.setVisibility(View.GONE);
            binding.progressIndicator.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
