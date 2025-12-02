package com.example.buddyppb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buddyppb.data.datastore.UserModel;
import com.example.buddyppb.data.datastore.UserPreference;
import com.example.buddyppb.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityWelcomeBinding binding;

    private final String FIELD_REQUIRED = "Field tidak boleh kosong";
    private UserModel userModel = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnMulai.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_mulai) {
            String name = binding.edtName.getText().toString().trim();
            boolean isFirstTime = false;

            if (name.isEmpty()) {
                binding.edtName.setError(FIELD_REQUIRED);
                return;
            }

            saveUser(name, isFirstTime);

            Intent intent = new Intent(this, OnboardingActivity.class);
            startActivity(intent);
            finish();

        }
    }

    private void saveUser(String name, boolean isFirstTime) {
        UserPreference userPreference = new UserPreference(this);

        UserModel userModel = new UserModel();

        userModel.setName(name);
        userModel.setFirstTime(isFirstTime);

        userPreference.setUser(userModel);
    }
}