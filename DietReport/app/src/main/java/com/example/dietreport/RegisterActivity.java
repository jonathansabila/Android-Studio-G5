package com.example.dietreport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etUsername, etPassword;
    Button btnRegister;
    MaterialToolbar toolbar;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Toolbar
        toolbar = findViewById(R.id.toolbarRegister);
        toolbar.setNavigationOnClickListener(v -> finish());

        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsernameRegister);
        etPassword = findViewById(R.id.etPasswordRegister);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new DBHelper(this);

        btnRegister.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // CHECK IF USERNAME EXISTS
            Cursor cursor = db.rawQuery(
                    "SELECT 1 FROM users WHERE username=? LIMIT 1",
                    new String[]{username}
            );
            if (cursor.moveToFirst()) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                return;
            }

            cursor.close();

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("username", username);
            values.put("password", password);

            long result = db.insert("users", null, values);

            if (result == -1) {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                db.close();
                return;
            }

            Toast.makeText(this,
                    "Registered Successfully",
                    Toast.LENGTH_SHORT).show();

            db.close();

            etName.setText("");
            etUsername.setText("");
            etPassword.setText("");
            finish();
        });
    }
}