package com.prankulgarg.accedo;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by prankulgarg on 7/25/17.
 */

public class Utils {

    public static void hideSoftKeyboard(Context context) {
        try {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            if (((Activity) context).getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(((Activity) context).getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public static void showError(Context context, View view, String message) {
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
