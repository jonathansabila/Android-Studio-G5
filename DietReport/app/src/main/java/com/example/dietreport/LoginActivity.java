package com.example.dietreport;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnGoRegister;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        dbHelper = new DBHelper(this);

        // OPTIONAL: check if no users exist
        SQLiteDatabase dbCheck = dbHelper.getReadableDatabase();
        Cursor c = dbCheck.rawQuery("SELECT COUNT(*) FROM users", null);
        c.moveToFirst();

        int count = c.getInt(0);
        c.close();

        if (count == 0) {
            Toast.makeText(this,
                    "No account found. Please register first.",
                    Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, RegisterActivity.class));
        }

        // LOGIN BUTTON
        btnLogin.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                etUsername.setError("Enter username");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Enter password");
                return;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT * FROM users WHERE username=? AND password=?",
                    new String[]{username, password}
            );

            if (cursor.getCount() > 0) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        });

        // REGISTER BUTTON (OUTSIDE LOGIN CLICK)
        btnGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}