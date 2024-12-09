package com.example.e_comerce.DatabaseAccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class CategoryDatabase extends SQLiteOpenHelper {



        private static final String TAG = "CategoryDatabase";

        // Database version and name (keep consistent with other database classes)
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "ECommerceDatabase.db";

        // Category table details
        public static final String TABLE_CATEGORIES = "categories";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";

        // Create table SQL statement
        private static final String CREATE_CATEGORIES_TABLE =
                "CREATE TABLE " + TABLE_CATEGORIES + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_NAME + " TEXT UNIQUE NOT NULL, "
                        + COLUMN_IMAGE + " BLOB)";

        public CategoryDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d(TAG, "CategoryDatabase constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(TAG, "onCreate method called");
            try {
                db.execSQL(CREATE_CATEGORIES_TABLE);
                Log.d(TAG, "Categories table created successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error creating categories table", e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "onUpgrade method called. Old version: " + oldVersion + ", New version: " + newVersion);

            // Drop existing table if exists
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);

            // Recreate table
            onCreate(db);
        }

        // Add a method to check if table exists
        public boolean isTableExists(SQLiteDatabase db) {
            if (db == null) {
                return false;
            }

            Cursor cursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                    new String[]{TABLE_CATEGORIES}
            );

            boolean exists = (cursor != null && cursor.getCount() > 0);

            if (cursor != null) {
                cursor.close();
            }

            return exists;
        }

}