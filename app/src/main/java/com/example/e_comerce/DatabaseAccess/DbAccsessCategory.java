package com.example.e_comerce.DatabaseAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.e_comerce.JavaClasses.Category;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DbAccsessCategory {
    private CategoryDatabase categoryDatabase;
    private SQLiteDatabase db;

    private static final String TAG = "DbAccsessCategory";

    public DbAccsessCategory(Context context) {
        // Initialize the database helper
        categoryDatabase = new CategoryDatabase(context);
    }

    // Method to convert Bitmap to byte array
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    // Method to add a new category with name and image
    public long addCategory(String categoryName, Bitmap categoryImage) {
        try {
            // Open the database in write mode
            db = categoryDatabase.getWritableDatabase();

            // Verify table exists
            if (!categoryDatabase.isTableExists(db)) {
                Log.e(TAG, "Categories table does not exist. Attempting to create.");
                categoryDatabase.onCreate(db);
            }

            ContentValues values = new ContentValues();
            values.put(CategoryDatabase.COLUMN_NAME, categoryName);

            byte[] imageBytes = bitmapToByteArray(categoryImage);
            values.put(CategoryDatabase.COLUMN_IMAGE, imageBytes);

            long newRowId = db.insert(CategoryDatabase.TABLE_CATEGORIES, null, values);

            Log.d(TAG, "Insertion result: " + newRowId);
            return newRowId;
        } catch (Exception e) {
            Log.e(TAG, "Error inserting category", e);
            e.printStackTrace();
            return -1;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // Method to retrieve all categories with id, name, and image
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // Open the database in read mode
            db = categoryDatabase.getReadableDatabase();

            // Query to select all categories
            cursor = db.query(CategoryDatabase.TABLE_CATEGORIES,
                    null,  // null means select all columns
                    null,  // no where clause
                    null,  // no where arguments
                    null,  // no group by
                    null,  // no having
                    null); // no order by

            // Check if cursor has data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Get column indices
                    int idIndex = cursor.getColumnIndex(CategoryDatabase.COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(CategoryDatabase.COLUMN_NAME);
                    int imageIndex = cursor.getColumnIndex(CategoryDatabase.COLUMN_IMAGE);

                    // Extract data
                    int categoryId = cursor.getInt(idIndex);
                    String categoryName = cursor.getString(nameIndex);
                    byte[] imageBytes = cursor.getBlob(imageIndex);

                    // Convert byte array to Bitmap
                    Bitmap categoryImage = null;
                    if (imageBytes != null) {
                        categoryImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    }

                    // Create Category object and add to list
                    // Note: Passing an empty list for products as per your original Category constructor
                    Category category = new Category();
                    category.id=categoryId;
                    category.name=categoryName;
                    category.imageBitmap=categoryImage;
                    categories.add(category);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving categories", e);
        } finally {
            // Close cursor and database
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return categories;
    }

    // Optional: Close database connection when done
    public void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    // Method to delete all data in the database
    public void deleteAllData() {
        // Open the database in write mode
        db = categoryDatabase.getWritableDatabase();

        // Delete all rows from the categories table
        int deletedRows = db.delete(CategoryDatabase.TABLE_CATEGORIES, null, null);

        Log.d(TAG, "All data deleted. Rows affected: " + deletedRows);

        // Close the database
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}