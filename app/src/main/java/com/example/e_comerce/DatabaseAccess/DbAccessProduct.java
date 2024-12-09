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
import java.util.ArrayList;
import java.util.List;

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
    public List<Object[]> getAllProductsWithCategories() {
        List<Object[]> productsList = new ArrayList<>();
        SQLiteDatabase db = productDatabase.getReadableDatabase();

        try {
            // SQL query to join products and categories tables
            String query = "SELECT " +
                    // Product columns
                    "p." + ProductDatabase.COLUMN_ID + " AS product_id, " +
                    "p." + ProductDatabase.COLUMN_NAME + " AS product_name, " +
                    "p." + ProductDatabase.COLUMN_PRICE + " AS product_price, " +
                    "p." + ProductDatabase.COLUMN_QUANTITY + " AS product_quantity, " +
                    "p." + ProductDatabase.COLUMN_IMAGE + " AS product_image, " +

                    // Category columns
                    "c." + CategoryDatabase.COLUMN_ID + " AS category_id, " +
                    "c." + CategoryDatabase.COLUMN_NAME + " AS category_name, " +
                    "c." + CategoryDatabase.COLUMN_IMAGE + " AS category_image " +

                    // Join condition
                    "FROM " + ProductDatabase.TABLE_PRODUCTS + " p " +
                    "LEFT JOIN " + CategoryDatabase.TABLE_CATEGORIES + " c " +
                    "ON p." + ProductDatabase.COLUMN_CATEGORY_ID + " = c." + CategoryDatabase.COLUMN_ID;

            Cursor cursor = db.rawQuery(query, null);

            // Check if cursor has results
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Retrieve product details
                    int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                    String productName = cursor.getString(cursor.getColumnIndexOrThrow("product_name"));
                    double productPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("product_price"));
                    int productQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("product_quantity"));

                    // Retrieve product image
                    Bitmap productImage = null;
                    byte[] productImageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("product_image"));
                    if (productImageBytes != null) {
                        productImage = BitmapFactory.decodeByteArray(productImageBytes, 0, productImageBytes.length);
                    }

                    // Retrieve category details
                    int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                    String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("category_name"));

                    // Retrieve category image
                    Bitmap categoryImage = null;
                    byte[] categoryImageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("category_image"));
                    if (categoryImageBytes != null) {
                        categoryImage = BitmapFactory.decodeByteArray(categoryImageBytes, 0, categoryImageBytes.length);
                    }

                    // Create an array to hold all retrieved information
                    Object[] productInfo = new Object[]{
                            productId,
                            productName,
                            productPrice,
                            productQuantity,
                            productImage,
                            categoryId,
                            categoryName,
                            categoryImage
                    };

                    productsList.add(productInfo);
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving products with categories", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return productsList;
    }
    // Optional: Close database connection when done
    public void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }







}