package com.example.e_comerce.DatabaseAccess.AccessingDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.e_comerce.DatabaseAccess.CreatingDataBase.ProductDatabase;
import com.example.e_comerce.JavaClasses.Product;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DbAccessProduct {
    private final ProductDatabase productDatabase;
    private SQLiteDatabase db;

    public DbAccessProduct(Context context) {
        productDatabase = new ProductDatabase(context);
    }

    // Open database connection
    public void open() {
        db = productDatabase.getWritableDatabase();
    }

    // Close database connection
    public void close() {
        productDatabase.close();
    }

    /**
     * Convert Bitmap to byte array for database storage
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
     * Add a new product to the database
     * @param product Product to add
     * @return row ID of the newly inserted product, or -1 if error
     */
    public long addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(ProductDatabase.COLUMN_CATEGORY_ID, 0); // You might want to pass category ID differently
        values.put(ProductDatabase.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductDatabase.COLUMN_PRODUCT_COST, product.getCost());
        values.put(ProductDatabase.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        values.put(ProductDatabase.COLUMN_PRODUCT_IMAGE, bitmapToByteArray(product.getImage()));

        return db.insert(ProductDatabase.TABLE_PRODUCTS, null, values);
    }

    /**
     * Delete a product by its ID
     * @param productId ID of the product to delete
     * @return number of rows affected
     */
    public int deleteProduct(int productId) {
        return db.delete(
                ProductDatabase.TABLE_PRODUCTS,
                ProductDatabase.COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)}
        );
    }

    /**
     * Edit product details
     * @param product Product with updated information
     * @return number of rows affected
     */
    public int editProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(ProductDatabase.COLUMN_CATEGORY_ID, 0); // You might want to pass category ID differently
        values.put(ProductDatabase.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductDatabase.COLUMN_PRODUCT_COST, product.getCost());
        values.put(ProductDatabase.COLUMN_PRODUCT_QUANTITY, product.getQuantity());
        values.put(ProductDatabase.COLUMN_PRODUCT_IMAGE, bitmapToByteArray(product.getImage()));

        return db.update(
                ProductDatabase.TABLE_PRODUCTS,
                values,
                ProductDatabase.COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getId())}
        );
    }

    /**
     * Get the price of a product by its ID
     * @param productId ID of the product
     * @return price of the product, or -1.0 if product not found
     */
    public double getProductPriceById(int productId) {
        String[] projection = {
                ProductDatabase.COLUMN_PRODUCT_COST
        };

        Cursor cursor = db.query(
                ProductDatabase.TABLE_PRODUCTS,
                projection,
                ProductDatabase.COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(productId)},
                null,
                null,
                null
        );

        double price = -1.0; // Default return value if product not found

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int costIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_PRODUCT_COST);
                price = cursor.getDouble(costIndex);
            }
            cursor.close();
        }

        return price;
    }

    /**
     * Get all products for a specific category
     * @param categoryId ID of the category
     * @return List of products in the specified category
     */
    public List<Product> getProductsByCategory(long categoryId) {
        List<Product> products = new ArrayList<>();

        String[] projection = {
                ProductDatabase.COLUMN_PRODUCT_ID,
                ProductDatabase.COLUMN_CATEGORY_ID,
                ProductDatabase.COLUMN_PRODUCT_NAME,
                ProductDatabase.COLUMN_PRODUCT_COST,
                ProductDatabase.COLUMN_PRODUCT_QUANTITY,
                ProductDatabase.COLUMN_PRODUCT_IMAGE
        };

        Cursor cursor = db.query(
                ProductDatabase.TABLE_PRODUCTS,
                projection,
                ProductDatabase.COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)},
                null,
                null,
                null
        );

        // Check if cursor has columns before processing
        if (cursor != null) {
            // Get column indices before looping
            int idIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_PRODUCT_ID);
            int categoryIdIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_CATEGORY_ID);
            int nameIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_PRODUCT_NAME);
            int costIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_PRODUCT_COST);
            int quantityIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_PRODUCT_QUANTITY);
            int imageIndex = cursor.getColumnIndexOrThrow(ProductDatabase.COLUMN_PRODUCT_IMAGE);

            // Iterate through cursor and create Product objects
            while (cursor.moveToNext()) {
                Product product = new Product(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getDouble(costIndex),
                        cursor.getInt(quantityIndex),
                        byteArrayToBitmap(cursor.getBlob(imageIndex))
                );
                products.add(product);
            }

            // Close the cursor
            cursor.close();
        }

        return products;
    }
}