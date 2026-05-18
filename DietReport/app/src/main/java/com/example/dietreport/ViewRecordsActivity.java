package com.example.dietreport;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class ViewRecordsActivity extends AppCompatActivity {

    private ArrayList<Record> recordList = new ArrayList<>();
    private RecyclerView rvRecords;
    private RecordAdapter adapter;
    private TextView tvEmpty;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);

        MaterialToolbar toolbar = findViewById(R.id.toolbarRecords);
        tvEmpty = findViewById(R.id.tvEmpty);
        rvRecords = findViewById(R.id.rvRecords);

        dbHelper = new DBHelper(this);
        rvRecords.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setNavigationOnClickListener(v -> finish());

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // refresh list when returning
    }

    private void loadData() {
        recordList.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM diet_records ORDER BY id DESC", null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String age = cursor.getString(cursor.getColumnIndexOrThrow("age"));
                String room = cursor.getString(cursor.getColumnIndexOrThrow("room"));
                String dateTime = cursor.getString(cursor.getColumnIndexOrThrow("date_time"));
                String religion = cursor.getString(cursor.getColumnIndexOrThrow("religion"));
                String diet = cursor.getString(cursor.getColumnIndexOrThrow("diet"));
                byte[] signature = cursor.getBlob(cursor.getColumnIndexOrThrow("signature"));

                recordList.add(new Record(id, name, age, room, dateTime, religion, diet, signature));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load records", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        if (recordList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvRecords.setVisibility(View.GONE);
            return;
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvRecords.setVisibility(View.VISIBLE);
        }

        // Set up adapter
        adapter = new RecordAdapter(this, recordList, position -> {
            // Item click: open RecordDetailActivity
            Intent intent = new Intent(ViewRecordsActivity.this, RecordDetailActivity.class);
            intent.putExtra("recordId", recordList.get(position).getId());
            startActivity(intent);
        });

        rvRecords.setAdapter(adapter);
    }

    public void deleteRecord(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleted = db.delete("diet_records", "id=?", new String[]{String.valueOf(id)});
        db.close();

        if (deleted > 0) {
            Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to delete record", Toast.LENGTH_SHORT).show();
        }

        loadData();
    }
}