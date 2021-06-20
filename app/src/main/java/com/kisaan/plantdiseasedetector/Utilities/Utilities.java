package com.kisaan.plantdiseasedetector.Utilities;

import android.content.Context;
import android.widget.Toast;

public class Utilities {
    private static Toast toast;

    public static boolean checkPhone(String s) {
        return (s != null && s.length() == 10 && s.charAt(0) >= '5' && s.charAt(0) <= '9');
    }

    public static void makeToast(String s, Context context) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        toast.show();
    }

}
