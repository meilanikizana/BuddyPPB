package com.example.buddyppb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

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
            } else {
                Toast.makeText(this, "Failed to save journal.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToDetailJournalPage() {
        String title = binding.etJournalTitle.getText().toString();
        String description = binding.etJournalContent.getText().toString();
        String imageUri = currentImageUri != null ? currentImageUri.toString() : "";

        Intent intent = new Intent(this, DetailJournalActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("imageUri", imageUri);

        startActivity(intent);
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
        String content = binding.etJournalContent.getText().toString();
        String image = currentImageUri != null ? currentImageUri.toString() : "";

        Journal journal = new Journal();
        journal.setTitle(title);
        journal.setImage(image);
        journal.setDescription(content);
        journal.setTimestamp(DateHelper.getCurrentDate());

        // SIMPAN SEKALI SAJA
        journalViewModel.insertJournalWithCallback(journal, insertedJournal -> {

            String today = DateHelper.getCurrentDate();
            journalViewModel.writeJournal(today);


            // Masukkan ke history
            journalViewModel.addJournalHistory(today, title);

//            // Update streak (WAJIB)
//            journalViewModel.writeJournal(today);

            // Lanjut ke detail
            Intent intent = new Intent(AddJournalActivity.this, DetailJournalActivity.class);
            intent.putExtra(DetailJournalActivity.EXTRA_JOURNAL, insertedJournal);
            startActivity(intent);
            finish();
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
