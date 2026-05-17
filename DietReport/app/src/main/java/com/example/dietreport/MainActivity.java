package com.example.dietreport;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private EditText editName, editAge, editRoom, editDateTime, editReligion, editDiet;
    private ImageView signatureView;
    private Button btnOpenPad, btnSubmit, btnClearPad, btnDonePad;
    private CardView signaturePadContainer;
    private SignatureView signaturePadView;
    private EditText editDate, editTime;
    private String selectedDate, selectedTime;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnViewRecords = findViewById(R.id.btnViewRecords);

        btnViewRecords.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewRecordsActivity.class));
        });

        dbHelper = new DBHelper(this);

        // Form fields
        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        editRoom = findViewById(R.id.editRoom);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        editReligion = findViewById(R.id.editReligion);
        editDiet = findViewById(R.id.editDiet);

        // FIXED: correct ID from XML
        signatureView = findViewById(R.id.signatureView);

        // AUTO TIME
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        selectedTime = sdf.format(Calendar.getInstance().getTime());
        editTime.setText(selectedTime);

        // DATE PICKER
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

        // Signature pad
        btnOpenPad = findViewById(R.id.btnOpenPad);
        signaturePadContainer = findViewById(R.id.signaturePadContainer);
        signaturePadView = findViewById(R.id.inlineSignatureView);
        btnClearPad = findViewById(R.id.btnClearPad);
        btnDonePad = findViewById(R.id.btnDonePad);

        btnSubmit = findViewById(R.id.btnSubmit);

        // Open pad
        btnOpenPad.setOnClickListener(v ->
                signaturePadContainer.setVisibility(View.VISIBLE)
        );

        // Clear pad
        btnClearPad.setOnClickListener(v ->
                signaturePadView.clear()
        );

        // Done pad
        btnDonePad.setOnClickListener(v -> {

            if (signaturePadView.isEmpty()) {
                Toast.makeText(this,
                        "Please sign first",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap bitmap = signaturePadView.getSignatureBitmap();

            signatureView.setImageBitmap(bitmap);
            signatureView.setVisibility(View.VISIBLE);

            signaturePadView.clear();
            signaturePadContainer.setVisibility(View.GONE);
        });

        // Submit
        btnSubmit.setOnClickListener(v -> {

            String name = editName.getText().toString().trim();
            String age = editAge.getText().toString().trim();
            String room = editRoom.getText().toString().trim();
            String date = editDate.getText().toString().trim();
            String time = editTime.getText().toString().trim();
            String religion = editReligion.getText().toString().trim();
            String diet = editDiet.getText().toString().trim();

            if (name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Fill required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();

            db.execSQL("INSERT INTO diet_records(name, age, room, date_time, religion, diet) VALUES(?,?,?,?,?,?)",
                    new Object[]{
                            name,
                            age,
                            room,
                            date + " " + time,
                            religion,
                            diet
                    });

            Toast.makeText(this, "Saved to database", Toast.LENGTH_SHORT).show();

            // optional clear
            editName.setText("");
            editAge.setText("");
            editRoom.setText("");
            editDateTime.setText("");
            editReligion.setText("");
            editDiet.setText("");
        });
    }
}