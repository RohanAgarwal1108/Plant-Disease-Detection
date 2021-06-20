package com.kisaan.plantdiseasedetector.authentication;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kisaan.plantdiseasedetector.Utilities.GenericKeyEvent;
import com.kisaan.plantdiseasedetector.Utilities.GenericTextWatcher;
import com.kisaan.plantdiseasedetector.Utilities.KeyboardUtil;
import com.kisaan.plantdiseasedetector.Utilities.MyProgressDialog;
import com.kisaan.plantdiseasedetector.Utilities.Utilities;
import com.kisaan.plantdiseasedetector.databinding.ActivityOtpBinding;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ActivityOtpBinding binding;
    FirebaseAuth mAuth;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    CountDownTimer cTimer;
    String etOtp;
    private String phno;
    private String Uid;
    private int flag = 1;
    private MyProgressDialog myProgressDialog;
    private PhoneAuthCredential credential;
    private String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phno = getIntent().getExtras().getString("phone");
        binding.phonenumber.setText(phno);
        mAuth = FirebaseAuth.getInstance();
        setListeners();
        setOTPListeners();
        initFireBaseCallbacks();
        sendOTP();
    }

    private void setListeners() {
        binding.edit.setOnClickListener(this);
        binding.backotp.setOnClickListener(this);
        binding.resend.setOnClickListener(this);
        binding.otpnext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.edit || v == binding.backotp) {
            onBackPressed();
        } else if (v == binding.resend) {
            if (binding.resend.getText().equals("Resend")) {
                sendOTP();
            }
        } else if (v == binding.otpnext) {
            if (binding.otpnext.getAlpha() == 1) {
                if (flag == 1) {
                    myProgressDialog.showDialog(this);
                    firebaseOTPCheck();
                } else if (flag == 2) {
                    Utilities.makeToast("OTP could'nt be sent. Please try again!", OtpActivity.this);
                }
            }
        }
    }

    private void sendOTP() {
        myProgressDialog = new MyProgressDialog();
        myProgressDialog.showDialog(this);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phno)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(mResendToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void firebaseOTPCheck() {
        credential = PhoneAuthProvider.getCredential(mVerificationId, etOtp);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        Uid = user.getUid();
                        getFcmToken();
                    } else {
                        myProgressDialog.dismissDialog();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Utilities.makeToast("Invalid credentials!", OtpActivity.this);
                        }
                    }
                });
    }

    private void getFcmToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        myProgressDialog.dismissDialog();
                        Utilities.makeToast("Please check your internet connection and try again later!", OtpActivity.this);
                        return;
                    }
                    fcmToken = task.getResult();
                    getUserbyPhone();
                });
    }

    private void getUserbyPhone() {
        checkPhone(phno)
                .addOnCompleteListener(task -> {
                    myProgressDialog.dismissDialog();
                    if (!task.isSuccessful()) {
                        whenfailed(task);
                    } else {
                        whenUserFound(task);
                    }
                });
    }

    private void whenUserFound(Task<HashMap<String, Object>> task) {
        HashMap<String, Object> result = task.getResult();
        HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
        String uid = (String) data.get("uid");
        String city = (String) data.get("city");
        String name = (String) data.get("name");
        try {
            MainActivity.putValues(MainActivity.ALIAS2, "filled", getApplicationContext());
            MainActivity.putValues(MainActivity.ALIAS4, uid, getApplicationContext());
            MainActivity.putValues(MainActivity.ALIAS3, name, getApplicationContext());
            MainActivity.putValues(MainActivity.ALIAS1, phno, getApplicationContext());
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        //todo go to next activity
    }

    //todo
    private void whenfailed(Task<HashMap<String, Object>> task) {
        Exception e = task.getException();
        if (e instanceof FirebaseFunctionsException) {
            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
            FirebaseFunctionsException.Code code = ffe.getCode();
            if (code == FirebaseFunctionsException.Code.NOT_FOUND) {
                try {
                    MainActivity.putValues(MainActivity.ALIAS1, phno, getApplicationContext());
                    MainActivity.putValues(MainActivity.ALIAS4, Uid, getApplicationContext());
                } catch (GeneralSecurityException | IOException generalSecurityException) {
                    generalSecurityException.printStackTrace();
                    return;
                }
                //todo
//                Intent intent = new Intent(Frame38.this, Frame47.class);
//                startActivity(intent);
            } else {
                //todo
//                Intent intent = new Intent(Frame38.this, Reconnect.class);
//                startActivity(intent);
            }
        } else {
//            Intent intent = new Intent(Frame38.this, Reconnect.class);
//            startActivity(intent);
        }
    }

    private Task<HashMap<String, Object>> checkPhone(String text) {
        Map<String, Object> data = new HashMap<>();
        data.put("phoneNo", text);
        data.put("fcm", fcmToken);
        return Utilities.mFunctions
                .getHttpsCallable("checkPhone")
                .call(data)
                .continueWith(task -> (HashMap<String, Object>) task.getResult().getData());
    }

    private void setOTPListeners() {
        binding.otp1.addTextChangedListener(new GenericTextWatcher(binding.otp2, binding.otp1, binding.otp1));
        binding.otp2.addTextChangedListener(new GenericTextWatcher(binding.otp3, binding.otp1, binding.otp2));
        binding.otp3.addTextChangedListener(new GenericTextWatcher(binding.otp4, binding.otp2, binding.otp3));
        binding.otp4.addTextChangedListener(new GenericTextWatcher(binding.otp5, binding.otp3, binding.otp3));
        binding.otp5.addTextChangedListener(new GenericTextWatcher(binding.otp6, binding.otp4, binding.otp5));
        binding.otp6.addTextChangedListener(new GenericTextWatcher(binding.otp6, binding.otp5, binding.otp6));
        binding.otp1.addTextChangedListener(this);
        binding.otp2.addTextChangedListener(this);
        binding.otp3.addTextChangedListener(this);
        binding.otp4.addTextChangedListener(this);
        binding.otp5.addTextChangedListener(this);
        binding.otp6.addTextChangedListener(this);
        binding.otp1.setOnKeyListener(new GenericKeyEvent(binding.otp1, null, binding.otp2));
        binding.otp2.setOnKeyListener(new GenericKeyEvent(binding.otp2, binding.otp1, binding.otp3));
        binding.otp3.setOnKeyListener(new GenericKeyEvent(binding.otp3, binding.otp2, binding.otp4));
        binding.otp4.setOnKeyListener(new GenericKeyEvent(binding.otp4, binding.otp3, binding.otp5));
        binding.otp5.setOnKeyListener(new GenericKeyEvent(binding.otp5, binding.otp4, binding.otp6));
        binding.otp6.setOnKeyListener(new GenericKeyEvent(binding.otp6, binding.otp5, null));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        etOtp = binding.otp1.getText().toString() + binding.otp2.getText().toString() +
                binding.otp3.getText().toString() + binding.otp4.getText().toString() +
                binding.otp5.getText().toString() + binding.otp6.getText().toString();
        if (etOtp.length() != 6) {
            disableNext();
        } else {
            enableNext();
        }
    }

    private void enableNext() {
        binding.otpnext.setAlpha(1);
    }

    private void disableNext() {
        binding.otpnext.setAlpha(0.5f);
    }

    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                myProgressDialog.dismissDialog();
                binding.otpnext.performClick();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                flag = 2;
                myProgressDialog.dismissDialog();
                Utilities.makeToast("OTP couldn't be sent. Please try again!", OtpActivity.this);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                myProgressDialog.dismissDialog();
                flag = 1;
                Utilities.makeToast("OTP Sent", OtpActivity.this);
                mVerificationId = verificationId;
                mResendToken = token;
                changeResend();
            }

            private void changeResend() {
                cTimer = new CountDownTimer(60000, 1000) {
                    //when timer is started
                    public void onTick(long millisUntilFinished) {
                        binding.didnt.setText("Resend code in ");
                        long i = millisUntilFinished / 1000;
                        binding.resend.setText((i >= 0 && i <= 9 ? "0:0" : "0:") + i);
                    }

                    //when timer is finished
                    public void onFinish() {
                        binding.resend.setText("Resend");
                        binding.didnt.setText("Didn't receive? ");
                    }
                };
                cTimer.start();
            }
        };
    }

    /**
     * Cancelling the timer
     */
    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    /**
     * Cancelling the timer when app is closed to avoid memory leak
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtil keyboardUtil = new KeyboardUtil(this, ev);
        keyboardUtil.touchEvent();
        return super.dispatchTouchEvent(ev);
    }
}