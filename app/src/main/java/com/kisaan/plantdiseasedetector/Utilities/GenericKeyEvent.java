package com.kisaan.plantdiseasedetector.Utilities;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.SuperCook.R;

public class GenericKeyEvent implements View.OnKeyListener {

    private final EditText currentView;
    private final EditText previousView;
    private final EditText nextView;

    /**
     * to change the edittext box based on entered key
     *
     * @param nextView     to get the next edittext
     * @param currentView  to get the edittext with the focus
     * @param previousView to get the edittext before the focused one
     */
    public GenericKeyEvent(EditText currentView, EditText previousView, EditText nextView) {
        this.currentView = currentView;
        this.previousView = previousView;
        this.nextView = nextView;
    }

    /**
     * to change otp box on entering or deleting number
     *
     * @param keyCode to get the key pressed
     * @param event   to find if the action performed by user is tap on otp
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        //if the text is deleted
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.getId() != R.id.otp1 && currentView.getText().length() == 0) {
            previousView.setText(null);
            previousView.requestFocus();
            return true;
        }
        //if a valid number is inputted
        else if (currentView.getId() != R.id.otp6 && currentView.getText().length() > 0) {
            nextView.requestFocus();
        }
        return false;
    }
}
