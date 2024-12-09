package com.example.e_comerce.DatabaseAccess;// ProductDatabase.java
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.e_comerce.DatabaseAccess.CategoryDatabase;

public class ProductDatabase extends SQLiteOpenHelper {
    private static final String TAG = "ProductDatabase";

    // Database version and name (keep consistent with other database classes)
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ECommerceDatabase.db";

    // Product table details
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_CATEGORY_ID = "category_id";

    // Create table SQL statement
    private static final String CREATE_PRODUCTS_TABLE =
            "CREATE TABLE " + TABLE_PRODUCTS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_NAME + " TEXT NOT NULL, "
                    + COLUMN_PRICE + " REAL NOT NULL, "
                    + COLUMN_QUANTITY + " INTEGER NOT NULL, "
                    + COLUMN_IMAGE + " BLOB, "
                    + COLUMN_CATEGORY_ID + " INTEGER, "
                    + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES "
                    + CategoryDatabase.TABLE_CATEGORIES + "(" + CategoryDatabase.COLUMN_ID + "))";

    public ProductDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "ProductDatabase constructor called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate method called");
        try {
            db.execSQL(CREATE_PRODUCTS_TABLE);
            Log.d(TAG, "Products table created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating products table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade method called. Old version: " + oldVersion + ", New version: " + newVersion);

        // Drop existing table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // Recreate table
        onCreate(db);
    }

    // Method to check if table exists
    public boolean isTableExists(SQLiteDatabase db) {
        if (db == null) {
            return false;
        }

        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{TABLE_PRODUCTS}
        );

        boolean exists = (cursor != null && cursor.getCount() > 0);

        if (cursor != null) {
            cursor.close();
        }

        return exists;
    }
}
