package com.example.e_comerce.DatabaseAccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TransactionDatabase extends SQLiteOpenHelper {
    private static final String TAG = "TransactionDatabase";

    // Database version and name (consistent with other database classes)
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ECommerceDatabase.db";

    // Transaction table details
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_DATE = "transaction_date";
    public static final String COLUMN_QUANTITY = "quantity";

    // Create table SQL statement
    private static final String CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                    + COLUMN_USER_ID + " INTEGER, "
                    + COLUMN_PRODUCT_ID + " INTEGER, "
                    + COLUMN_DATE + " TEXT, "
                    + COLUMN_QUANTITY + " INTEGER, "
                    + "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_PRODUCT_ID + ", " + COLUMN_DATE + "), "
                    + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES "
                    + CustomerDatabase.TABLE_CUSTOMERS + "(" + CustomerDatabase.COLUMN_ID + "), "
                    + "FOREIGN KEY(" + COLUMN_PRODUCT_ID + ") REFERENCES "
                    + ProductDatabase.TABLE_PRODUCTS + "(" + ProductDatabase.COLUMN_ID + "))";

    public TransactionDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "TransactionDatabase constructor called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate method called");
        try {
            db.execSQL(CREATE_TRANSACTIONS_TABLE);
            Log.d(TAG, "Transactions table created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating transactions table", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade method called. Old version: " + oldVersion + ", New version: " + newVersion);

        // Drop existing table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);

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
                new String[]{TABLE_TRANSACTIONS}
        );

        boolean exists = (cursor != null && cursor.getCount() > 0);

        if (cursor != null) {
            cursor.close();
        }

        return exists;
    }
}