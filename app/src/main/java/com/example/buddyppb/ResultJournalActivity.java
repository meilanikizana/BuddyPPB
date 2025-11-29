package com.example.buddyppb;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.annotation.Nullable;

import com.example.buddyppb.databinding.ActivityResultJournalBinding;
import com.example.buddyppb.helper.ViewUtils;


public class ResultJournalActivity extends AppCompatActivity {

    private ActivityResultJournalBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResultJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewUtils.setupView(getWindow(), getSupportActionBar());

        // Get data from intent
        String content = getIntent().getStringExtra(EXTRA_CONTENT);
        if (content == null) content = "";

        String positiveWords = getIntent().getStringExtra(EXTRA_POSITIVE_WORDS);
        String negativeWords = getIntent().getStringExtra(EXTRA_NEGATIVE_WORDS);

        int positiveCount = getIntent().getIntExtra(EXTRA_POSITIVE_COUNT, 0);
        int negativeCount = getIntent().getIntExtra(EXTRA_NEGATIVE_COUNT, 0);

        binding.tvJournalContent.setText(content);

        // Positive words
        if (positiveWords == null || positiveWords.isEmpty()) {
            binding.tvListPositiveWords.setText(
                    getString(R.string.positive_words, getString(R.string.empty))
            );
        } else {
            binding.tvListPositiveWords.setText(
                    getString(R.string.positive_words, positiveWords)
            );
        }

        // Negative words
        if (negativeWords == null || negativeWords.isEmpty()) {
            binding.tvListNegativeWords.setText(
                    getString(R.string.negative_words, getString(R.string.empty))
            );
        } else {
            binding.tvListNegativeWords.setText(
                    getString(R.string.negative_words, negativeWords)
            );
        }

        binding.positiveCount.setText(String.valueOf(positiveCount));
        binding.negativeCount.setText(String.valueOf(negativeCount));

        // Positive & negative results
        String positiveResult = getIntent().getStringExtra(EXTRA_POSITIVE_RESULT);
        String negativeResult = getIntent().getStringExtra(EXTRA_NEGATIVE_RESULT);

        binding.tvPositiveResult.setText(positiveResult);
        binding.tvNegativeResult.setText(negativeResult);

        // Switch view
        binding.btnChange.setOnClickListener(v -> {

            View positiveTitle = binding.tvPositiveTitle;
            View positivePercentage = binding.tvPositiveResult;
            View negativeTitle = binding.tvNegativeTitle;
            View negativePercentage = binding.tvNegativeResult;

            if (positiveTitle.getVisibility() == View.VISIBLE &&
                    positivePercentage.getVisibility() == View.VISIBLE) {

                positiveTitle.setVisibility(View.INVISIBLE);
                positivePercentage.setVisibility(View.INVISIBLE);

                negativeTitle.setVisibility(View.VISIBLE);
                negativePercentage.setVisibility(View.VISIBLE);
            } else {
                positiveTitle.setVisibility(View.VISIBLE);
                positivePercentage.setVisibility(View.VISIBLE);

                negativeTitle.setVisibility(View.INVISIBLE);
                negativePercentage.setVisibility(View.INVISIBLE);
            }
        });

        // See More
        binding.tvSeeMore.setOnClickListener(v -> {
            binding.tvJournalContentTitle.setVisibility(View.VISIBLE);
            binding.tvJournalContent.setVisibility(View.VISIBLE);
            binding.tvListPositiveWords.setVisibility(View.VISIBLE);
            binding.tvListNegativeWords.setVisibility(View.VISIBLE);
            binding.tvSeeMore.setVisibility(View.GONE);
            binding.ivBuddyLove.setVisibility(View.GONE);
            binding.tvCloseSeeMore.setVisibility(View.VISIBLE);
        });

        // Close See More
        binding.tvCloseSeeMore.setOnClickListener(v -> {
            binding.tvJournalContentTitle.setVisibility(View.GONE);
            binding.tvJournalContent.setVisibility(View.GONE);
            binding.tvListPositiveWords.setVisibility(View.GONE);
            binding.tvListNegativeWords.setVisibility(View.GONE);
            binding.tvSeeMore.setVisibility(View.VISIBLE);
            binding.ivBuddyLove.setVisibility(View.VISIBLE);
            binding.tvCloseSeeMore.setVisibility(View.GONE);
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }

    public static final String EXTRA_CONTENT = "extra_content";
    public static final String EXTRA_POSITIVE_RESULT = "extra_positive_result";
    public static final String EXTRA_NEGATIVE_RESULT = "extra_negative_result";
    public static final String EXTRA_POSITIVE_COUNT = "extra_positive_count";
    public static final String EXTRA_NEGATIVE_COUNT = "extra_negative_count";
    public static final String EXTRA_POSITIVE_WORDS = "extra_positive_words";
    public static final String EXTRA_NEGATIVE_WORDS = "extra_negative_words";
}
