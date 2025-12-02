package com.example.buddyppb.helper;

import android.os.Build;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;

public class ViewUtils {

    public static void setupView(Window window, ActionBar supportActionBar) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (window.getInsetsController() != null) {
                window.getInsetsController().hide(WindowInsets.Type.statusBars());
            }
        } else {
            // Deprecated method but required for older Android versions
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        if (supportActionBar != null) {
            supportActionBar.hide();
        }
    }
}
