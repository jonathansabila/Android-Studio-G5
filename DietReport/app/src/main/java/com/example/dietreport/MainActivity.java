package com.example.dietreport;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editName, editAge, editDate, editTime, editReligion;
    private Spinner spinnerRoom, spinnerDiet;
    private ImageView signatureView;
    private Button btnOpenPad, btnSubmit, btnClearPad, btnDonePad;
    private CardView signaturePadContainer;
    private SignatureView signaturePadView;

    private String selectedDate, selectedTime;
    private boolean hasSignature = false;
    private byte[] signatureBytes;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbarRecords);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> {

            new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });

        dbHelper = new DBHelper(this);

        // Initialize Views
        initViews();

        // Setup spinners
        setupSpinners();

        // Auto-fill current time
        setAutoTime();

        // Date picker
        setupDatePicker();

        // Signature pad logic
        setupSignaturePad();

        // Submit button logic
        setupSubmitButton();

        // View records button
        Button btnViewRecords = findViewById(R.id.btnViewRecords);
        btnViewRecords.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ViewRecordsActivity.class))
        );
    }

    private void initViews() {
        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        editReligion = findViewById(R.id.editReligion);

        spinnerRoom = findViewById(R.id.spinnerRoom);
        spinnerDiet = findViewById(R.id.spinnerDiet);

        signatureView = findViewById(R.id.signatureView);
        signaturePadContainer = findViewById(R.id.signaturePadContainer);
        signaturePadView = findViewById(R.id.inlineSignatureView);

        btnOpenPad = findViewById(R.id.btnOpenPad);
        btnClearPad = findViewById(R.id.btnClearPad);
        btnDonePad = findViewById(R.id.btnDonePad);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void setupSpinners() {
        String[] rooms = {"101", "102", "103", "201", "202", "203"};
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rooms);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(roomAdapter);

        String[] diets = {"Regular", "Vegetarian", "Vegan", "Gluten-Free", "Diabetic"};
        ArrayAdapter<String> dietAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, diets);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiet.setAdapter(dietAdapter);
    }

    private void setAutoTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        selectedTime = sdf.format(Calendar.getInstance().getTime());
        editTime.setText(selectedTime);
    }

    private void setupDatePicker() {
        editDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (DatePicker view, int year, int month, int dayOfMonth) -> {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void setupSignaturePad() {
        btnOpenPad.setOnClickListener(v -> signaturePadContainer.setVisibility(View.VISIBLE));
        signatureView.setVisibility(View.GONE);
        btnClearPad.setOnClickListener(v -> signaturePadView.clear());

        btnDonePad.setOnClickListener(v -> {
            if (signaturePadView.isEmpty()) {
                Toast.makeText(this, "Please sign first", Toast.LENGTH_SHORT).show();
                return;
            }

            signaturePadView.post(() -> {
                Bitmap bitmap = signaturePadView.getSignatureBitmap();
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    signatureBytes = stream.toByteArray();
                    hasSignature = true;

                    signatureView.setImageBitmap(bitmap);
                    signatureView.setVisibility(View.VISIBLE);

                    signaturePadView.clear();
                    signaturePadContainer.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, "Signature capture failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        signatureView.setOnClickListener(v -> signaturePadContainer.setVisibility(View.VISIBLE));
    }

    private void setupSubmitButton() {
        btnSubmit.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String age = editAge.getText().toString().trim();
            String room = spinnerRoom.getSelectedItem().toString();
            String diet = spinnerDiet.getSelectedItem().toString();
            String date = editDate.getText().toString().trim();
            String time = editTime.getText().toString().trim();
            String religion = editReligion.getText().toString().trim();

            if (name.isEmpty() || age.isEmpty() || room.isEmpty()
                    || date.isEmpty() || time.isEmpty()
                    || religion.isEmpty() || diet.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!hasSignature || signatureBytes == null) {
                Toast.makeText(this, "Please add signature", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(
                    "INSERT INTO diet_records(name, age, room, date_time, religion, diet, signature) VALUES(?,?,?,?,?,?,?)",
                    new Object[]{name, age, room, date + " " + time, religion, diet, signatureBytes}
            );
            db.close();

            Toast.makeText(this, "Saved to database", Toast.LENGTH_SHORT).show();

            resetForm();
        });
    }

    private void resetForm() {
        editName.setText("");
        editAge.setText("");
        editDate.setText("");
        editReligion.setText("");
        spinnerRoom.setSelection(0);
        spinnerDiet.setSelection(0);

        selectedTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Calendar.getInstance().getTime());
        editTime.setText(selectedTime);

        hasSignature = false;
        signatureBytes = null;
        signatureView.setImageDrawable(null);
        signatureView.setVisibility(View.INVISIBLE);
    }
}