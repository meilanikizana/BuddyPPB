package com.example.buddyppb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buddyppb.databinding.ActivityOnboardingBinding;

public class OnboardingActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityOnboardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnMulai.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null && v.getId() == R.id.btn_mulai) {
            Intent intent = new Intent(this, JournalActivity.class);
            startActivity(intent);
        }
    }
}