package com.kisaan.plantdiseasedetector.Utilities;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class GenericTextWatcher implements TextWatcher {
    private final EditText etPrev;
    private final EditText etNext;
    private final EditText etPres;

    /**
     * To change the otp edittext box when a number is entered
     *
     * @param etNext to get the next edittext
     * @param etPres to get the edittext with the focus
     * @param etPrev to get the edittext before the focused one
     */
    public GenericTextWatcher(EditText etNext, EditText etPrev, EditText etPres) {
        this.etPrev = etPrev;
        this.etNext = etNext;
        this.etPres = etPres;
    }

    /**
     * Changing edittext focus on text change
     */
    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (text.length() == 1)
            etNext.requestFocus();
        else if (text.length() == 0)
            etPrev.requestFocus();
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }
}

