package com.example.dietreport;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class RecordDetailActivity extends AppCompatActivity {

    private EditText editName, editAge, editDate, editReligion;
    private Spinner spinnerRoom, spinnerDiet;
    private ImageView imgSignature;
    private Button btnUpdate;

    private DBHelper dbHelper;
    private int recordId;
    private byte[] signatureBytes; // store current signature
    private boolean hasSignature = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        dbHelper = new DBHelper(this);

        // Get recordId from Intent
        recordId = getIntent().getIntExtra("recordId", -1);
        if (recordId == -1) {
            Toast.makeText(this, "Invalid record", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        editDate = findViewById(R.id.editDate);
        editReligion = findViewById(R.id.editReligion);
        spinnerRoom = findViewById(R.id.spinnerRoom);
        spinnerDiet = findViewById(R.id.spinnerDiet);
        imgSignature = findViewById(R.id.imgSignature);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Populate spinners
        String[] rooms = {"101", "102", "103", "201", "202", "203"};
        spinnerRoom.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rooms));

        String[] diets = {"Regular", "Vegetarian", "Vegan", "Gluten-Free", "Diabetic"};
        spinnerDiet.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, diets));

        // Load record from DB
        loadRecord();

        // Date picker
        editDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RecordDetailActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Update button
        btnUpdate.setOnClickListener(v -> updateRecord());
    }

    private void loadRecord() {
        // Query the DB
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM diet_records WHERE id = ?", new String[]{String.valueOf(recordId)});

        if (cursor.moveToFirst()) {
            editName.setText(cursor.getString(1));
            editAge.setText(cursor.getString(2));
            String room = cursor.getString(3);
            editDate.setText(cursor.getString(4).split(" ")[0]); // just date part
            editReligion.setText(cursor.getString(5));
            String diet = cursor.getString(6);
            signatureBytes = cursor.getBlob(7);

            // Set spinner selections
            setSpinnerSelection(spinnerRoom, room);
            setSpinnerSelection(spinnerDiet, diet);

            // Set signature image
            if (signatureBytes != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(signatureBytes, 0, signatureBytes.length);
                imgSignature.setImageBitmap(bmp);
                hasSignature = true;
            }
        } else {
            Toast.makeText(this, "Record not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        cursor.close();
        db.close();
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(value);
        if (position >= 0) spinner.setSelection(position);
    }

    private void updateRecord() {
        String name = editName.getText().toString().trim();
        String age = editAge.getText().toString().trim();
        String room = spinnerRoom.getSelectedItem().toString();
        String diet = spinnerDiet.getSelectedItem().toString();
        String date = editDate.getText().toString().trim();
        String religion = editReligion.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || room.isEmpty() || diet.isEmpty() || date.isEmpty() || religion.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!hasSignature || signatureBytes == null) {
            Toast.makeText(this, "Signature missing", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(
                "UPDATE diet_records SET name=?, age=?, room=?, date_time=?, religion=?, diet=?, signature=? WHERE id=?",
                new Object[]{name, age, room, date, religion, diet, signatureBytes, recordId}
        );
        db.close();

        Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT).show();
        finish();
    }
}