package com.example.e_comerce.DatabaseAccess;

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

    public List<Category> getAllCategoriesWithProducts() {
        List<Category> categoriesWithProductsList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // Open the database in read mode
            db = categoryDatabase.getReadableDatabase();

            // Check if database is null
            if (db == null) {
                Log.e(TAG, "Database is null. Cannot retrieve categories.");
                return categoriesWithProductsList;
            }

            // Query to select all categories
            cursor = db.query(
                    CategoryDatabase.TABLE_CATEGORIES,
                    new String[]{
                            CategoryDatabase.COLUMN_ID,
                            CategoryDatabase.COLUMN_NAME,
                            CategoryDatabase.COLUMN_IMAGE
                    },
                    null, null, null, null, null
            );

            // Check if cursor is null
            if (cursor == null) {
                Log.e(TAG, "Cursor is null. No categories found or query failed.");
                return categoriesWithProductsList;
            }

            // Log the total number of categories
            Log.d(TAG, "Total categories found: " + cursor.getCount());

            // Check if cursor has data
            if (cursor.moveToFirst()) {
                do {
                    // Get category column indices
                    int categoryIdIndex = cursor.getColumnIndex(CategoryDatabase.COLUMN_ID);
                    int categoryNameIndex = cursor.getColumnIndex(CategoryDatabase.COLUMN_NAME);
                    int categoryImageIndex = cursor.getColumnIndex(CategoryDatabase.COLUMN_IMAGE);

                    // Extract category data
                    int categoryId = cursor.getInt(categoryIdIndex);
                    String categoryName = cursor.getString(categoryNameIndex);
                    byte[] categoryImageBytes = cursor.getBlob(categoryImageIndex);

                    // Convert category image to Bitmap
                    Bitmap categoryImage = null;
                    if (categoryImageBytes != null) {
                        categoryImage = BitmapFactory.decodeByteArray(categoryImageBytes, 0, categoryImageBytes.length);
                    }

                    // Now fetch all products for this category
                    List<Product> productsInCategory = getProductsForCategory(db, categoryId);

                    // Log the number of products for this category
                    Log.d(TAG, "Category: " + categoryName + ", Products count: " + productsInCategory.size());

                    // Create a new Category object
                    Category category = new Category();
                    category.name = categoryName;
                    category.Products = productsInCategory;
                    category.imageBitmap = categoryImage;
                    category.id = categoryId;

                    categoriesWithProductsList.add(category);

                } while (cursor.moveToNext());
            } else {
                Log.e(TAG, "No categories found in the database.");
            }

            // Log the total number of categories with products
            Log.d(TAG, "Total categories with products: " + categoriesWithProductsList.size());

            return categoriesWithProductsList;

        } catch (Exception e) {
            Log.e(TAG, "Error retrieving categories with products", e);
            return categoriesWithProductsList;
        } finally {
            // Close cursor
            if (cursor != null) {
                cursor.close();
            }
            // Close database
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    // Helper method to get products for a specific category
    private List<Product> getProductsForCategory(SQLiteDatabase db, int categoryId) {
        List<Product> productsList = new ArrayList<>();
        Cursor productCursor = null;

        try {
            // Query to select all products for this category
            productCursor = db.query(
                    ProductDatabase.TABLE_PRODUCTS,
                    null,  // Select all columns
                    ProductDatabase.COLUMN_CATEGORY_ID + " = ?",  // Where clause
                    new String[]{String.valueOf(categoryId)},  // Where arguments
                    null, null, null
            );

            if (productCursor != null && productCursor.moveToFirst()) {
                do {
                    // Get product column indices
                    int productIdIndex = productCursor.getColumnIndex(ProductDatabase.COLUMN_ID);
                    int productNameIndex = productCursor.getColumnIndex(ProductDatabase.COLUMN_NAME);
                    int productPriceIndex = productCursor.getColumnIndex(ProductDatabase.COLUMN_PRICE);
                    int productQuantityIndex = productCursor.getColumnIndex(ProductDatabase.COLUMN_QUANTITY);
                    int productImageIndex = productCursor.getColumnIndex(ProductDatabase.COLUMN_IMAGE);

                    // Extract product data
                    int productId = productCursor.getInt(productIdIndex);
                    String productName = productCursor.getString(productNameIndex);
                    double productPrice = productCursor.getDouble(productPriceIndex);
                    int productQuantity = productCursor.getInt(productQuantityIndex);

                    // Convert product image to Bitmap
                    Bitmap productImage = null;
                    byte[] productImageBytes = productCursor.getBlob(productImageIndex);
                    if (productImageBytes != null) {
                        productImage = BitmapFactory.decodeByteArray(productImageBytes, 0, productImageBytes.length);
                    }

                    // Create a new Product object
                    Product product = new Product(productId, productName, productPrice, productQuantity, productImage);

                    productsList.add(product);

                } while (productCursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving products for category", e);
        } finally {
            // Close product cursor
            if (productCursor != null) {
                productCursor.close();
            }
        }

        return productsList;
    }

}