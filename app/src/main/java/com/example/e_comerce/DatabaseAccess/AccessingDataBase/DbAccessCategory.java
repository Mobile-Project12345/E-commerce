package com.example.e_comerce.DatabaseAccess.AccessingDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.e_comerce.DatabaseAccess.CreatingDataBase.CategoryDatabase;
import com.example.e_comerce.JavaClasses.Category;
import com.example.e_comerce.JavaClasses.Product;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DbAccessCategory {
    private final CategoryDatabase categoryDatabase;
    private final DbAccessProduct dbAccessProduct;
    private SQLiteDatabase db;

    public DbAccessCategory(Context context) {
        categoryDatabase = new CategoryDatabase(context);
        dbAccessProduct = new DbAccessProduct(context);
    }

    // Open database connection
    public void open() {
        db = categoryDatabase.getWritableDatabase();
        dbAccessProduct.open(); // Ensure product database is also open
    }

    // Close database connection
    public void close() {
        categoryDatabase.close();
        dbAccessProduct.close();
    }

    /**
     * Convert Bitmap to byte array
     * @param bitmap Bitmap to convert
     * @return byte array representation of the bitmap
     */
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * Convert byte array to Bitmap
     * @param byteArray byte array to convert
     * @return Bitmap representation of the byte array
     */
    private Bitmap byteArrayToBitmap(byte[] byteArray) {
        if (byteArray == null) return null;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
     * Add a new category
     * @param name Category name
     * @param image Category image
     * @return row ID of the newly inserted category, or -1 if error
     */
    public long addCategory(String name, Bitmap image) {
        ContentValues values = new ContentValues();
        values.put(CategoryDatabase.COLUMN_CATEGORY_NAME, name);
        values.put(CategoryDatabase.COLUMN_CATEGORY_IMAGE, bitmapToByteArray(image));

        return db.insert(CategoryDatabase.TABLE_CATEGORIES, null, values);
    }

    /**
     * Delete a category and all its associated products
     * @param categoryId ID of the category to delete
     * @return number of categories deleted
     */
    public int deleteCategory(long categoryId) {
        // First, delete all products in this category
        dbAccessProduct.open(); // Ensure the product database is open
        dbAccessProduct.getProductsByCategory(categoryId).forEach(product ->
                dbAccessProduct.deleteProduct(product.getId())
        );

        // Then delete the category
        return db.delete(
                CategoryDatabase.TABLE_CATEGORIES,
                CategoryDatabase.COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)}
        );
    }

    /**
     * Edit category name
     * @param categoryId ID of the category to edit
     * @param newName New name for the category
     * @return number of rows affected
     */
    public int editCategoryName(long categoryId, String newName) {
        ContentValues values = new ContentValues();
        values.put(CategoryDatabase.COLUMN_CATEGORY_NAME, newName);

        return db.update(
                CategoryDatabase.TABLE_CATEGORIES,
                values,
                CategoryDatabase.COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)}
        );
    }

    /**
     * Edit category image
     * @param categoryId ID of the category to edit
     * @param newImage New image for the category
     * @return number of rows affected
     */
    public int editCategoryImage(long categoryId, Bitmap newImage) {
        ContentValues values = new ContentValues();
        values.put(CategoryDatabase.COLUMN_CATEGORY_IMAGE, bitmapToByteArray(newImage));

        return db.update(
                CategoryDatabase.TABLE_CATEGORIES,
                values,
                CategoryDatabase.COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)}
        );
    }

    /**
     * Get all categories
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        Cursor cursor = db.query(
                CategoryDatabase.TABLE_CATEGORIES,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndexOrThrow(CategoryDatabase.COLUMN_CATEGORY_NAME);
            int imageIndex = cursor.getColumnIndexOrThrow(CategoryDatabase.COLUMN_CATEGORY_IMAGE);
            int idIndex = cursor.getColumnIndexOrThrow(CategoryDatabase.COLUMN_CATEGORY_ID);

            while (cursor.moveToNext()) {
                long categoryId = cursor.getLong(idIndex);
                String name = cursor.getString(nameIndex);
                byte[] imageBytes = cursor.getBlob(imageIndex);
                Bitmap image = byteArrayToBitmap(imageBytes);

                // Fetch products for this category
                List<Product> products = dbAccessProduct.getProductsByCategory(categoryId);

                Category category = new Category(name, products, image);
                categories.add(category);
            }

            cursor.close();
        }

        return categories;
    }
}