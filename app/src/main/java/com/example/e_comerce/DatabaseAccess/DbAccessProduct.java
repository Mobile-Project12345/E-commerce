package com.example.e_comerce.DatabaseAccess;


// DbAccessProduct.java
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.e_comerce.JavaClasses.Category;
import com.example.e_comerce.JavaClasses.Product;

import java.io.ByteArrayOutputStream;

public class DbAccessProduct {
    private static final String TAG = "DbAccessProduct";

    private ProductDatabase productDatabase;
    private SQLiteDatabase db;

    public DbAccessProduct(Context context) {
        // Initialize the database helper
        productDatabase = new ProductDatabase(context);
    }

    // Method to convert Bitmap to byte array
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
    // Method to get a product by ID
    public Product getProductById(int productId) {
        SQLiteDatabase db = productDatabase.getReadableDatabase();
        Product product = null;

        // Query to find product by ID
        String query = "SELECT * FROM " + ProductDatabase.TABLE_PRODUCTS +
                " WHERE " + ProductDatabase.COLUMN_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            // Retrieve column indices
            int nameIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_NAME);
            int priceIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_PRICE);
            int quantityIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_QUANTITY);
            int imageIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_IMAGE);

            // Extract data from cursor
            String name = cursor.getString(nameIndex);
            Double cost = cursor.getDouble(priceIndex);
            int quantity = cursor.getInt(quantityIndex);

            // Convert byte array to Bitmap
            Bitmap image = null;
            byte[] imageBytes = cursor.getBlob(imageIndex);
            if (imageBytes != null) {
                image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }

            // Create Product object
            product = new Product(productId, name, cost, quantity, image);
        }

        // Close cursor and database
        cursor.close();
        db.close();

        return product;  // Returns null if no product found with the given ID
    }
    // Method to add a new product
    public long addProduct(String productName, double price, int quantity, Bitmap productImage, int CategoryId) {
        try {
            // Open the database in write mode
            db = productDatabase.getWritableDatabase();

            // Verify table exists
            if (!productDatabase.isTableExists(db)) {
                Log.e(TAG, "Products table does not exist. Attempting to create.");
                productDatabase.onCreate(db);
            }

            // Create a new map of values to insert
            ContentValues values = new ContentValues();
            values.put(ProductDatabase.COLUMN_NAME, productName);
            values.put(ProductDatabase.COLUMN_PRICE, price);
            values.put(ProductDatabase.COLUMN_QUANTITY, quantity);
            values.put(ProductDatabase.COLUMN_CATEGORY_ID, CategoryId);

            // Convert bitmap to byte array and add to values
            byte[] imageBytes = bitmapToByteArray(productImage);
            values.put(ProductDatabase.COLUMN_IMAGE, imageBytes);

            // Insert the new row, returning the ID of the new row
            long newRowId = db.insert(ProductDatabase.TABLE_PRODUCTS, null, values);

            Log.d(TAG, "Insertion result: " + newRowId);
            return newRowId; //9
        } catch (Exception e) {
            Log.e(TAG, "Error inserting product", e);
            e.printStackTrace();
            return -1;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // Optional: Close database connection when done
    public void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}