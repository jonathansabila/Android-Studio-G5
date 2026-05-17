package com.example.dietreport;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewRecordsActivity extends AppCompatActivity {

    ListView lvRecords;
    ArrayList<String> list;
    ArrayAdapter adapter;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);

        lvRecords = findViewById(R.id.lvRecords);
        dbHelper = new DBHelper(this);

        loadData();
    }

    private void loadData() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM diet_records", null);

        list = new ArrayList<>();

        while (cursor.moveToNext()) {

            String item =
                    "Name: " + cursor.getString(1) + "\n" +
                            "Age: " + cursor.getString(2) + "\n" +
                            "Room: " + cursor.getString(3) + "\n" +
                            "Date: " + cursor.getString(4) + "\n" +
                            "Religion: " + cursor.getString(5) + "\n" +
                            "Diet: " + cursor.getString(6);

            list.add(item);
        }

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                list);

        lvRecords.setAdapter(adapter);

        cursor.close();
    }
}