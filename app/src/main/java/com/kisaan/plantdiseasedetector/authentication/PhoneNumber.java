package com.kisaan.plantdiseasedetector.authentication;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kisaan.plantdiseasedetector.Utilities.Utilities;
import com.kisaan.plantdiseasedetector.databinding.ActivityPhoneNumberBinding;

public class PhoneNumber extends AppCompatActivity implements View.OnClickListener {
    private ActivityPhoneNumberBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setListeners();
    }

    private void setListeners() {
        binding.phonenext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.phonenext) {
            if (Utilities.checkPhone(binding.phonenumber.getText().toString())) {
                //todo send otp
            } else {
                Utilities.makeToast("Please enter a valid phone number!", PhoneNumber.this);
            }
        }
    }
}