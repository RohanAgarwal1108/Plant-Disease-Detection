package com.kisaan.plantdiseasedetector.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        binding.phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    disableNext();
                } else {
                    enableNext();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void disableNext() {
        binding.phonenext.setAlpha(0.5f);
    }

    private void enableNext() {
        binding.phonenext.setAlpha(1);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.phonenext) {
            if (binding.phonenext.getAlpha() == 1) {
                if (Utilities.checkPhone(binding.phonenumber.getText().toString())) {
                    //todo
                    Intent intent = new Intent(PhoneNumber.this, OtpActivity.class);
                    intent.putExtra("phone", binding.phonenumber.getText().toString().trim());
                    startActivity(intent);
                } else {
                    Utilities.makeToast("Please enter a valid phone number!", PhoneNumber.this);
                }
            }
        }
    }
}