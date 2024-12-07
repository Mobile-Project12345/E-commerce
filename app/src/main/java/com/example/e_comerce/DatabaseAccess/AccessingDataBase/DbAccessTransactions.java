/*package com.example.e_comerce.DatabaseAccess.AccessingDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_comerce.DatabaseAccess.CreatingDataBase.TransactionDatabase;
import com.example.e_comerce.JavaClasses.TransactionData;

import java.util.ArrayList;
import java.util.List;

public class DbAccessTransactions {
    private final TransactionDatabase transactionDatabase;
    private final DbAccessProduct dbAccessProduct;
    private SQLiteDatabase db;

    public DbAccessTransactions(Context context) {
        transactionDatabase = new TransactionDatabase(context);
        dbAccessProduct = new DbAccessProduct(context);
    }

    // Open database connection
    public void open() {
        db = transactionDatabase.getWritableDatabase();
        dbAccessProduct.open();
    }

    // Close database connection
    public void close() {
        transactionDatabase.close();
        dbAccessProduct.close();
    }

    /**
     * Add or update a transaction
     * @param userEmail User's email
     * @param productId Product ID
     * @param transactionDate Transaction date
     * @return row ID of the transaction
     */
/*
    public long addTransaction(TransactionData transactionDate) {
        // Get product price
        double productPrice = dbAccessProduct.getProductPriceById(productId);
        productPrice=productPrice*quantity;
        // First, check if an existing transaction exists
        Cursor cursor = db.query(
                TransactionDatabase.TABLE_TRANSACTIONS,
                null,
                TransactionDatabase.COLUMN_USER_EMAIL + " = ? AND " +
                        TransactionDatabase.COLUMN_PRODUCT_ID + " = ? AND " +
                        TransactionDatabase.COLUMN_TRANSACTION_DATE + " = ?",
                new String[]{userEmail, String.valueOf(productId), transactionDate},
                null,
                null,
                null
        );

        ContentValues values = new ContentValues();
        long result;

        if (cursor != null && cursor.moveToFirst()) {
            // Existing transaction found - update
            int currentQuantityIndex = cursor.getColumnIndex(TransactionDatabase.COLUMN_QUANTITY);
            int currentQuantity = cursor.getInt(currentQuantityIndex);

            int newQuantity = currentQuantity + 1;
            double newTotalCost = newQuantity * productPrice;

            values.put(TransactionDatabase.COLUMN_QUANTITY, newQuantity);
            values.put(TransactionDatabase.COLUMN_TOTAL_COST, newTotalCost);

            result = db.update(
                    TransactionDatabase.TABLE_TRANSACTIONS,
                    values,
                    TransactionDatabase.COLUMN_USER_EMAIL + " = ? AND " +
                            TransactionDatabase.COLUMN_PRODUCT_ID + " = ? AND " +
                            TransactionDatabase.COLUMN_TRANSACTION_DATE + " = ?",
                    new String[]{userEmail, String.valueOf(productId), transactionDate}
            );

            cursor.close();
        } else {
            // New transaction

            values.put(TransactionDatabase.COLUMN_USER_EMAIL, userEmail);
            values.put(TransactionDatabase.COLUMN_PRODUCT_ID, productId);
            values.put(TransactionDatabase.COLUMN_TRANSACTION_DATE, transactionDate);
            values.put(TransactionDatabase.COLUMN_QUANTITY, quantity);
            values.put(TransactionDatabase.COLUMN_TOTAL_COST, productPrice);

            result = db.insert(TransactionDatabase.TABLE_TRANSACTIONS, null, values);
        }

        return result;
    }

    /**
     * Get all transactions
     * @return List of TransactionData
     */

/*
    public List<TransactionData> getAllTransactions() {
        List<TransactionData> transactions = new ArrayList<>();

        Cursor cursor = db.query(
                TransactionDatabase.TABLE_TRANSACTIONS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            int emailIndex = cursor.getColumnIndex(TransactionDatabase.COLUMN_USER_EMAIL);
            int productIdIndex = cursor.getColumnIndex(TransactionDatabase.COLUMN_PRODUCT_ID);
            int dateIndex = cursor.getColumnIndex(TransactionDatabase.COLUMN_TRANSACTION_DATE);
            int quantityIndex = cursor.getColumnIndex(TransactionDatabase.COLUMN_QUANTITY);
            int totalCostIndex = cursor.getColumnIndex(TransactionDatabase.COLUMN_TOTAL_COST);

            while (cursor.moveToNext()) {
                TransactionData transaction = new TransactionData(
                        cursor.getString(emailIndex),
                        cursor.getInt(productIdIndex),
                        "", // Note: Product name is not stored in this table
                        cursor.getString(dateIndex),
                        cursor.getInt(quantityIndex),
                        cursor.getDouble(totalCostIndex)
                );
                transactions.add(transaction);
            }

            cursor.close();
        }

        return transactions;
    }
}



*/