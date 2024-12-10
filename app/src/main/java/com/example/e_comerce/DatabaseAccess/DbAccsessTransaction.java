package com.example.e_comerce.DatabaseAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.e_comerce.JavaClasses.Customer;
import com.example.e_comerce.JavaClasses.Product;
import com.example.e_comerce.JavaClasses.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbAccsessTransaction {
    private static final String TAG = "DbAccessTransac";
    private TransactionDatabase transactionDatabase;
    private SQLiteDatabase db;
    Context context;
    public DbAccsessTransaction(Context context) {
        // Initialize the database helper
        this.context=context;
        transactionDatabase = new TransactionDatabase(context);
    }

    // Method to add a new transaction or update existing transaction
    public long addTransaction(int userId, int productId, String transactionDate, int quantity) {


        int requiredQuntity=quantity;

        Product ProductInDataBase=new Product();
         DbAccessProduct dbAccessProduct=new DbAccessProduct(context);
        ProductInDataBase=dbAccessProduct.getProductById(productId);
         if (ProductInDataBase.getQuantity()<requiredQuntity )
             return -1;




        try {
            // Open the database in write mode
            db = transactionDatabase.getWritableDatabase();

            // Verify table exists if not
            if (!transactionDatabase.isTableExists(db)) {
                Log.e(TAG, "Transactions table does not exist. Attempting to create.");
                transactionDatabase.onCreate(db);
            }

            // First, check if the transaction already exists
            String[] columns = {TransactionDatabase.COLUMN_QUANTITY};
            String selection = TransactionDatabase.COLUMN_USER_ID + " = ? AND " +
                    TransactionDatabase.COLUMN_PRODUCT_ID + " = ? AND " +
                    TransactionDatabase.COLUMN_DATE + " = ?";
            String[] selectionArgs = {String.valueOf(userId), String.valueOf(productId), transactionDate};

            Cursor cursor = db.query(
                    TransactionDatabase.TABLE_TRANSACTIONS,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            long result;
            if (cursor != null && cursor.moveToFirst()) {
                // Transaction exists, update the quantity
                int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(TransactionDatabase.COLUMN_QUANTITY));

                ContentValues updateValues = new ContentValues();
                updateValues.put(TransactionDatabase.COLUMN_QUANTITY, currentQuantity + quantity);

                result = db.update(
                        TransactionDatabase.TABLE_TRANSACTIONS,
                        updateValues,
                        selection,
                        selectionArgs
                );

                Log.d(TAG, "Updated existing transaction. Quantity increased by: " + quantity);
                cursor.close();
            } else {
                // Transaction does not exist, insert new row
                ContentValues values = new ContentValues();
                values.put(TransactionDatabase.COLUMN_USER_ID, userId);
                values.put(TransactionDatabase.COLUMN_PRODUCT_ID, productId);
                values.put(TransactionDatabase.COLUMN_DATE, transactionDate);
                values.put(TransactionDatabase.COLUMN_QUANTITY, quantity);

                result = db.insert(TransactionDatabase.TABLE_TRANSACTIONS, null, values);
                Log.d(TAG, "Inserted new transaction. Row ID: " + result);

                if (cursor != null) {
                    cursor.close();
                }
            }


            if (result>-1)
            {
                int newQantity=ProductInDataBase.getQuantity()-requiredQuntity;
                ProductInDataBase.setQuantity(newQantity);
                dbAccessProduct.UpdateProduct(ProductInDataBase);
            }
            return result;
        } catch (Exception e) {
            Log.e(TAG, "Error processing transaction", e);
            e.printStackTrace();
            return -1;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

    }



    public List<transaction> getAllTransactions() {
        SQLiteDatabase db = transactionDatabase.getReadableDatabase();
        List<transaction> transactionList = new ArrayList<>();

        // Projection - columns to retrieve
        String[] projection = {
                TransactionDatabase.COLUMN_USER_ID,
                TransactionDatabase.COLUMN_PRODUCT_ID,
                TransactionDatabase.COLUMN_DATE,
                TransactionDatabase.COLUMN_QUANTITY
        };

        Cursor cursor = null;

        try {
            // Query the database for all transactions
            cursor = db.query(
                    TransactionDatabase.TABLE_TRANSACTIONS, // Table name
                    projection,                             // Columns to retrieve
                    null,                                   // No WHERE clause (get all rows)
                    null,                                   // No selection args
                    null,                                   // No GROUP BY
                    null,                                   // No HAVING
                    null                                    // No ORDER BY
            );

            // Check if there are any transactions
            if (cursor == null || cursor.getCount() == 0) {
                Log.d("TransactionData", "No transactions found.");
                return transactionList; // Return empty list
            }

            // Initialize access to related data
            DbCustomerAccses customerAccess = new DbCustomerAccses(context);
            DbAccessProduct productAccess = new DbAccessProduct(context);

            // Iterate through the results
            while (cursor.moveToNext()) {
                // Retrieve data from each column
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(TransactionDatabase.COLUMN_USER_ID));
                int productId = cursor.getInt(cursor.getColumnIndexOrThrow(TransactionDatabase.COLUMN_PRODUCT_ID));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(TransactionDatabase.COLUMN_DATE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(TransactionDatabase.COLUMN_QUANTITY));

                // Fetch related customer and product details
                Customer customer = customerAccess.getCustomerById(userId);
                Product product = productAccess.getProductById(productId);

                // Build transaction object
                transaction transactionData = new transaction();
                transactionData.CustomerEmail = (customer != null) ? customer.Email : "Unknown";
                transactionData.Date = date;
                transactionData.product = product;
                transactionData.quantity=quantity;

                // Add to list
                transactionList.add(transactionData);
            }

            Log.d("TransactionData", "Transactions retrieved successfully.");
        } catch (Exception e) {
            Log.e("TransactionData", "Error fetching transactions: ", e);
        } finally {
            // Ensure the cursor is closed
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            // Ensure the database is closed
            if (db.isOpen()) {
                db.close();
            }
        }

        return transactionList;
    }
    // Optional: Close database connection when done
    public void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
    }
