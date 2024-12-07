package com.example.e_comerce.DatabaseAccess.CreatingDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomerDatabase extends SQLiteOpenHelper {
    // Database version and name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ECommerceDatabase.db";

    // Customer table details
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_BIRTHDATE = "birthdate";

    // Create table SQL statement
    private static final String CREATE_CUSTOMERS_TABLE =
            "CREATE TABLE " + TABLE_CUSTOMERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, "
                    + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, "
                    + COLUMN_PASSWORD + " TEXT NOT NULL, "
                    + COLUMN_BIRTHDATE + " TEXT NOT NULL)";

    public CustomerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_CUSTOMERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);

        // Recreate tables
        onCreate(db);
    }







}