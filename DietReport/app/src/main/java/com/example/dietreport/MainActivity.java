package com.example.dietreport;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private EditText editName, editAge, editRoom, editDateTime, editReligion, editDiet;
    private ImageView editSignature;
    private Button btnOpenPad, btnSubmit, btnClearPad, btnDonePad;
    private CardView signaturePadContainer;
    private SignatureView signaturePadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize form fields
        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        editRoom = findViewById(R.id.editRoom);
        editDateTime = findViewById(R.id.editDateTime);
        editReligion = findViewById(R.id.editReligion);
        editDiet = findViewById(R.id.editDiet);
        editSignature = findViewById(R.id.editSignature);
        
        // Initialize signature pad components
        btnOpenPad = findViewById(R.id.btnOpenPad);
        signaturePadContainer = findViewById(R.id.signaturePadContainer);
        signaturePadView = findViewById(R.id.inlineSignatureView);
        btnClearPad = findViewById(R.id.btnClearPad);
        btnDonePad = findViewById(R.id.btnDonePad);
        
        btnSubmit = findViewById(R.id.btnSubmit);

        // Open signature pad when button is clicked
        btnOpenPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePadContainer.setVisibility(View.VISIBLE);
            }
        });

        // Clear button inside signature pad
        btnClearPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePadView.clear();
            }
        });

        // Done button: Capture drawing as bitmap and display it
        btnDonePad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = signaturePadView.getSignatureBitmap();
                if (bitmap != null) {
                    editSignature.setImageBitmap(bitmap);
                    editSignature.setVisibility(View.VISIBLE);
                }
                signaturePadContainer.setVisibility(View.GONE);
            }
        });

        // Form submission
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, getString(R.string.toast_enter_name), Toast.LENGTH_SHORT).show();
                } else {
                    String message = String.format(getString(R.string.toast_submitted), name);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}