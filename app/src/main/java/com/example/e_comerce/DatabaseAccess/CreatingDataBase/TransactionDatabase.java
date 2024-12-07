package com.example.e_comerce.DatabaseAccess.CreatingDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransactionDatabase extends SQLiteOpenHelper {
    // Use the same database name and version as ProductDatabase
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ECommerceDatabase.db";

    // Transaction table details
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_PRODUCT_ID = "product_id";
    public static final String COLUMN_TRANSACTION_DATE = "transaction_date";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_TOTAL_COST = "total_cost"; // New column

    // Create table SQL statement
    private static final String CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                    + COLUMN_USER_EMAIL + " TEXT NOT NULL, "
                    + COLUMN_PRODUCT_ID + " INTEGER NOT NULL, "
                    + COLUMN_TRANSACTION_DATE + " TEXT NOT NULL, "
                    + COLUMN_QUANTITY + " INTEGER NOT NULL, "
                    + COLUMN_TOTAL_COST + " REAL NOT NULL, " // Add total cost column
                    + "PRIMARY KEY(" + COLUMN_USER_EMAIL + ", "
                    + COLUMN_PRODUCT_ID + ", "
                    + COLUMN_TRANSACTION_DATE + "), "
                    + "FOREIGN KEY(" + COLUMN_PRODUCT_ID + ") REFERENCES "
                    + ProductDatabase.TABLE_PRODUCTS + "(" + ProductDatabase.COLUMN_PRODUCT_ID + ")"
                    + ")";

    public TransactionDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create transactions table
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);

        // Recreate table
        onCreate(db);
    }

    /**
     * Add or update a transaction
     * If a transaction with same email, product ID, and date exists, increase the quantity
     *
     * @param userEmail User's email
     * @param productId Product ID
     * @param date Transaction date
     * @param quantity Quantity to add
     * @param productPrice Price of the product
     * @return long The row ID of the inserted or updated transaction
     */
    public long addOrUpdateTransaction(String userEmail, int productId, String date,
                                       int quantity, double productPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Calculate total cost
        double totalCost = quantity * productPrice;

        // First, check if a transaction with same email, product ID, and date exists
        Cursor cursor = null;
        long result;

        try {
            cursor = db.query(TABLE_TRANSACTIONS,
                    new String[]{COLUMN_QUANTITY, COLUMN_TOTAL_COST},
                    COLUMN_USER_EMAIL + " = ? AND " +
                            COLUMN_PRODUCT_ID + " = ? AND " +
                            COLUMN_TRANSACTION_DATE + " = ?",
                    new String[]{userEmail, String.valueOf(productId), date},
                    null, null, null);

            ContentValues values = new ContentValues();

            if (cursor != null && cursor.moveToFirst()) {
                // Get the indices safely
                int quantityColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_QUANTITY);
                int totalCostColumnIndex = cursor.getColumnIndexOrThrow(COLUMN_TOTAL_COST);

                // Transaction exists, update quantity and total cost
                int existingQuantity = cursor.getInt(quantityColumnIndex);
                double existingTotalCost = cursor.getDouble(totalCostColumnIndex);

                values.put(COLUMN_QUANTITY, existingQuantity + quantity);
                values.put(COLUMN_TOTAL_COST, existingTotalCost + totalCost);

                result = db.update(TABLE_TRANSACTIONS, values,
                        COLUMN_USER_EMAIL + " = ? AND " +
                                COLUMN_PRODUCT_ID + " = ? AND " +
                                COLUMN_TRANSACTION_DATE + " = ?",
                        new String[]{userEmail, String.valueOf(productId), date});
            } else {
                // Transaction doesn't exist, insert new
                values.put(COLUMN_USER_EMAIL, userEmail);
                values.put(COLUMN_PRODUCT_ID, productId);
                values.put(COLUMN_TRANSACTION_DATE, date);
                values.put(COLUMN_QUANTITY, quantity);
                values.put(COLUMN_TOTAL_COST, totalCost);

                result = db.insertOrThrow(TABLE_TRANSACTIONS, null, values);
            }
        } catch (IllegalArgumentException e) {
            // Handle column index error
            e.printStackTrace();
            result = -1;
        } finally {
            // Ensure cursor is closed
            if (cursor != null) {
                cursor.close();
            }
        }

        db.close();
        return result;
    }
}