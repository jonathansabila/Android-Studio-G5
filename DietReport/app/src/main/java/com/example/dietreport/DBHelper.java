package com.example.dietreport;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "DietDB.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "INSERT INTO users(name, username, password) VALUES('Admin','admin','1234')"
        );

        // USERS TABLE (FOR LOGIN + REGISTER)
        db.execSQL(
                "CREATE TABLE users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT," +
                        "username TEXT," +
                        "password TEXT)"
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
                        "diet TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS diet_records");
        onCreate(db);
    }
}