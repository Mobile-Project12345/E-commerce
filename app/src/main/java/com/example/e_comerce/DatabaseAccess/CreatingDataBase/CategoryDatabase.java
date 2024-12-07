package com.example.e_comerce.DatabaseAccess.CreatingDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CategoryDatabase extends SQLiteOpenHelper {
    // Database version and name (match with CustomerDatabase)
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ECommerceDatabase.db";

    // Category table details
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "name";
    public static final String COLUMN_CATEGORY_IMAGE = "image";

    // Create table SQL statement
    private static final String CREATE_CATEGORIES_TABLE =
            "CREATE TABLE " + TABLE_CATEGORIES + "("
                    + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_CATEGORY_NAME + " TEXT UNIQUE NOT NULL, "
                    + COLUMN_CATEGORY_IMAGE + " BLOB)";

    public CategoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create categories table
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

        // Recreate table
        onCreate(db);
    }
}