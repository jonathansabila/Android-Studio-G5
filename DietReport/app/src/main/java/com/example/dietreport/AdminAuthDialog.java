package com.example.dietreport;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

public class AdminAuthDialog {
    public interface Callback {
        void onResult(boolean success);
    }

    public static void show(Context context, Callback callback) {
        EditText input = new EditText(context);
        input.setHint("Enter Admin Password");

        new AlertDialog.Builder(context)
                .setTitle("Admin Authentication")
                .setView(input)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String pass = input.getText().toString().trim();
                    // simple hardcoded check
                    callback.onResult("admin123".equals(pass));
                })
                .setNegativeButton("Cancel", (dialog, which) -> callback.onResult(false))
                .show();
    }
}