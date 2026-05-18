package com.example.dietreport;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "DietDB.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        // USERS TABLE
        db.execSQL(
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "username TEXT UNIQUE," +
                        "password TEXT)"
        );

        // DEFAULT ADMIN USER
        db.execSQL(
                "INSERT OR IGNORE INTO users(name, username, password) VALUES('Admin','admin','1234')"
        );

        // DIET RECORDS TABLE
        db.execSQL(
                "CREATE TABLE diet_records (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "age TEXT," +
                        "room TEXT," +
                        "date_time TEXT," +
                        "religion TEXT," +
                        "diet TEXT," +
                        "signature BLOB)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS diet_records");
        onCreate(db);
    }
}