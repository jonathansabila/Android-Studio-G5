package com.example.dietreport;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etUsername, etPassword;
    Button btnRegister;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("username", username);
            values.put("password", password);

            db.insert("users", null, values);

            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();

            finish(); // go back to login
        });
    }
}