package com.example.buddyppb;

import static com.example.buddyppb.helper.JournalAnalyzerHelper.journalAnalyzerHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buddyppb.data.Journal;
import com.example.buddyppb.data.ResultJournal;
import com.example.buddyppb.databinding.ActivityDetailJournalBinding;
import android.app.Application;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.buddyppb.helper.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailJournalActivity extends AppCompatActivity {

    private ActivityDetailJournalBinding binding;
    private JournalViewModel journalViewModel;

    private Journal journal;
    private String positivePercentage;
    private String negativePercentage;
    private int positiveCount = 0;
    private int negativeCount = 0;
    private String positiveWordsString;
    private String negativeWordsString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewUtils.setupView(getWindow(), getSupportActionBar());

        journalViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(new Application()))
                .get(JournalViewModel.class);

// ViewModel
        journalViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getApplication()))
                .get(JournalViewModel.class);

// 1. Coba ambil parcelable (untuk setelah ADD)
        journal = getIntent().getParcelableExtra(EXTRA_JOURNAL);

        if (journal != null) {
            // langsung tampilkan data tanpa ambil dari database
            showDetail(journal);
        }
        else {
            // 2. Kalau parcelable null → berarti dipanggil dari RecyclerView
            int journalId = getIntent().getIntExtra("journal_id", -1);

            if (journalId != -1) {
                journalViewModel.getJournalById(journalId);
                observeDetailJournal();
            }
        }


        binding.btnBack.setOnClickListener(v -> finish());
        binding.fabEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddJournalActivity.class);
            intent.putExtra(EXTRA_JOURNAL, journal);
            startActivity(intent);
            finish();
        });

//        if (journal != null) {
//            Glide.with(this)
//                    .load(journal.getImage())
//                    .placeholder(R.drawable.default_image_buddy)
//                    .into(binding.ivJournalCover);
//            binding.tvJournalTitle.setText(journal.getTitle());
//            binding.tvJournalContent.setText(journal.getDescription());
//        }

//        binding.btnDelete.setOnClickListener(v -> alertDeleteJournal());
        binding.btnBack.setOnClickListener(v -> finish());
        binding.fabEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddJournalActivity.class);
            intent.putExtra(EXTRA_JOURNAL, journal);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (journal != null && journal.getId() > 0) {
            journalViewModel.getJournalById(journal.getId());
            observeDetailJournal();
        }
    }


    private void observeDetailJournal() {
        journalViewModel.getDetailJournal().observe(this, journalResult -> {
            if (journalResult != null) {

                // UPDATE UI DI SINI
                Glide.with(this)
                        .load(journalResult.getImage())
                        .placeholder(R.drawable.default_image_buddy)
                        .into(binding.ivJournalCover);

                binding.tvJournalTitle.setText(journalResult.getTitle());
                binding.tvJournalContent.setText(journalResult.getDescription());

                // UPDATE VARIABLE journal
                journal = journalResult;

                // SETUP ANALYZE BUTTON
                setupAnalyzeButton(journalResult);
            }
        });
    }

    private void showDetail(Journal journal) {
        Glide.with(this)
                .load(journal.getImage())
                .placeholder(R.drawable.default_image_buddy)
                .into(binding.ivJournalCover);

        binding.tvJournalTitle.setText(journal.getTitle());
        binding.tvJournalContent.setText(journal.getDescription());

        setupAnalyzeButton(journal);
    }


    private void setupAnalyzeButton(Journal journal) {
        if (journal.isAnalyzed()) {
            binding.btnAnalyze.setText(getString(R.string.see_result));
            binding.btnAnalyze.setOnClickListener(v -> {
                showLoading(true);
                showAnalyzeResult();
            });
        } else {
            binding.btnAnalyze.setText(getString(R.string.analisis_jurnal));
            binding.btnAnalyze.setOnClickListener(v -> {
                showLoading(true);
                String content = binding.tvJournalContent.getText().toString();
                analyzeJournal(this, content);
            });
        }
    }

    private void showAnalyzeResult() {
        showLoading(true);

        journalViewModel.getResultJournalLiveData().observe(this, resultJournal -> {
            showLoading(false);
            if (resultJournal != null) {
                positivePercentage = resultJournal.getPositivePercentage();
                negativePercentage = resultJournal.getNegativePercentage();
                positiveCount = resultJournal.getPositiveCount();
                negativeCount = resultJournal.getNegativeCount();
                positiveWordsString = resultJournal.getPositiveWords();
                negativeWordsString = resultJournal.getNegativeWords();

                Intent intent = new Intent(DetailJournalActivity.this, ResultJournalActivity.class);
                intent.putExtra(ResultJournalActivity.EXTRA_CONTENT, binding.tvJournalContent.getText().toString());
                intent.putExtra(ResultJournalActivity.EXTRA_POSITIVE_RESULT, positivePercentage);
                intent.putExtra(ResultJournalActivity.EXTRA_NEGATIVE_RESULT, negativePercentage);
                intent.putExtra(ResultJournalActivity.EXTRA_POSITIVE_COUNT, positiveCount);
                intent.putExtra(ResultJournalActivity.EXTRA_NEGATIVE_COUNT, negativeCount);
                intent.putExtra(ResultJournalActivity.EXTRA_POSITIVE_WORDS, positiveWordsString);
                intent.putExtra(ResultJournalActivity.EXTRA_NEGATIVE_WORDS, negativeWordsString);
                startActivity(intent);
            }
        });

        if (journal != null && journal.getId() > 0) {
            journalViewModel.getResultJournal(journal.getId());
        }
    }

    private void analyzeJournal(Context context, String inputText) {
        float[] sentimentScoresFloat = journalAnalyzerHelper(context, inputText);
        double[] sentimentScores = new double[sentimentScoresFloat.length];
        for (int i = 0; i < sentimentScoresFloat.length; i++) {
            sentimentScores[i] = sentimentScoresFloat[i];
        }


        double negativityScore = sentimentScores[0];
        double positivityScore = sentimentScores[1];

        positivePercentage = String.format("%.2f%%", positivityScore * 100);
        negativePercentage = String.format("%.2f%%", negativityScore * 100);

        List<String> positiveWords = Arrays.asList(
                "bahagia", "senang", "baik", "keren", "hebat", "positif", "bagus", "terbaik", "bersyukur",
                "syukur", "terbaik", "menyenangkan", "sukses", "semangat", "ceria", "cinta", "kuat",
                "tenang", "pintar", "damai", "sabar", "tulus", "berani", "bersyukur"
        );

        List<String> negativeWords = Arrays.asList(
                "sedih", "kecewa", "lelah", "capek", "sulit", "jelek", "buruk", "gagal", "negatif",
                "kesal", "nangis", "menyerah", "frustrasi", "marah", "tertekan", "cemas", "stress",
                "gagal", "hancur", "malas", "terpuruk", "kacau", "benci"
        );

        String[] words = inputText.split("[ .,!?;:]");
        List<String> positiveWordsList = new ArrayList<>();
        List<String> negativeWordsList = new ArrayList<>();

        for (String word : words) {
            if (positiveWords.contains(word.toLowerCase())) {
                positiveCount++;
                positiveWordsList.add(word);
            } else if (negativeWords.contains(word.toLowerCase())) {
                negativeCount++;
                negativeWordsList.add(word);
            }
        }

        positiveWordsString = String.join(", ", positiveWordsList);
        negativeWordsString = String.join(", ", negativeWordsList);

        showLoading(false);
        saveAndMoveToResult();
    }

    private void saveAndMoveToResult() {
        if (journal != null && journal.getId() >0) {
            journalViewModel.analyzeStatusUpdate(journal.getId(), true);
        }

        int journalIdValue = (journal != null) ? journal.getId() : 0;

        ResultJournal resultJournal = new ResultJournal(
                0,                    // resultId, karena Room autoGenerate
                journalIdValue,       // journalId
                positivePercentage,
                negativePercentage,
                positiveCount,
                negativeCount,
                positiveWordsString,
                negativeWordsString
        );

        journalViewModel.saveResultJournal(resultJournal);

        Intent intent = new Intent(this, ResultJournalActivity.class);
        intent.putExtra(ResultJournalActivity.EXTRA_CONTENT, binding.tvJournalContent.getText().toString());
        intent.putExtra(ResultJournalActivity.EXTRA_POSITIVE_RESULT, positivePercentage);
        intent.putExtra(ResultJournalActivity.EXTRA_NEGATIVE_RESULT, negativePercentage);
        intent.putExtra(ResultJournalActivity.EXTRA_POSITIVE_COUNT, positiveCount);
        intent.putExtra(ResultJournalActivity.EXTRA_NEGATIVE_COUNT, negativeCount);
        intent.putExtra(ResultJournalActivity.EXTRA_POSITIVE_WORDS, positiveWordsString);
        intent.putExtra(ResultJournalActivity.EXTRA_NEGATIVE_WORDS, negativeWordsString);
        startActivity(intent);
    }

//    private void alertDeleteJournal() {
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.alert_delete_journal, null);
//        TextView btnDelete = dialogView.findViewById(R.id.btn_delete);
//        TextView btnCancel = dialogView.findViewById(R.id.btn_cancel);
//
//        AlertDialog alertDialog = new AlertDialog.Builder(this)
//                .setView(dialogView)
//                .setCancelable(false)
//                .create();
//
//        btnDelete.setOnClickListener(v -> {
//            if (journal != null) {
//                journalViewModel.deleteJournal(journal);
//            }
//            finish();
//        });
//
//        btnCancel.setOnClickListener(v -> alertDialog.dismiss());
//        alertDialog.show();
//    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            binding.progressIndicator.setVisibility(View.VISIBLE);
            binding.progressIndicator.show();
        } else {
            binding.progressIndicator.setVisibility(View.INVISIBLE);
            binding.progressIndicator.hide();
        }
    }

    public static final String EXTRA_JOURNAL = "extra_journal";
}
