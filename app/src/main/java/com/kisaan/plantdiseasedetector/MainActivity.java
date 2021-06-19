package com.kisaan.plantdiseasedetector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    public static String ALIAS3 = "name";
    public static String ALIAS4 = "uid";
    public static String ALIAS1 = "phonenumber";
    public static String ALIAS2 = "status";
    static String masterKeyAlias = null;
    private static SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.yellow));
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(this::checkSignedInUser, 3 * 1000);
    }

    private void checkSignedInUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            try {
                String str = getValue(MainActivity.this, ALIAS2);
                if (str == null) {
//                    Intent intent = new Intent(MainActivity.this, Frame47.class);
//                    startActivity(intent);
                } else if (str.equals("filled")) {
//                    Intent intent = new Intent(MainActivity.this, Frame101.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
                }else {
//                    Intent intent = new Intent(MainActivity.this, Frame47.class);
//                    startActivity(intent);
                }
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(MainActivity.this, Frame39.class);
//                startActivity(intent);
            }
        } else {
//            Intent intent = new Intent(MainActivity.this, Frame39.class);
//            startActivity(intent);
        }
        finish();
    }

    private static void createPrefInstance(Context context) throws GeneralSecurityException, IOException {
        masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        sharedPreferences = EncryptedSharedPreferences.create(
                "kisaan_shared_prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static String getValue(Context context, String Alias) throws GeneralSecurityException, IOException {
        if (masterKeyAlias == null || sharedPreferences == null) {
            createPrefInstance(context);
        }
        if (sharedPreferences.contains(Alias)) {
            return sharedPreferences.getString(Alias, null);
        } else {
            return null;
        }
    }

    public static void putValues(String Alias, String value, Context context) throws GeneralSecurityException, IOException {
        if (masterKeyAlias == null || sharedPreferences == null) {
            createPrefInstance(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Alias, value);
        editor.apply();
    }



    public static void removeValue(Context context, String[] alias) throws GeneralSecurityException, IOException {
        if (masterKeyAlias == null || sharedPreferences == null) {
            createPrefInstance(context);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < alias.length; i++) {
            if (sharedPreferences.contains(alias[i])) {
                editor.remove(alias[i]);
            }
        }
        editor.apply();
    }
}