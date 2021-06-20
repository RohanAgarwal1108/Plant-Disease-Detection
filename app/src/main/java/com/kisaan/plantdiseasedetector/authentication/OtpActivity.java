package com.kisaan.plantdiseasedetector.authentication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kisaan.plantdiseasedetector.databinding.ActivityOtpBinding;

public class OtpActivity extends AppCompatActivity {
    private ActivityOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}