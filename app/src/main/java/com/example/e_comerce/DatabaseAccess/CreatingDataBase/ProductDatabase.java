package com.example.e_comerce.DatabaseAccess.CreatingDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDatabase extends SQLiteOpenHelper {
    // Database version and name (match with other databases)
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ECommerceDatabase.db";

    // Product table details
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PRODUCT_ID = "id";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_COST = "cost";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    public static final String COLUMN_PRODUCT_IMAGE = "image";

    // Create table SQL statement
    private static final String CREATE_PRODUCTS_TABLE =
            "CREATE TABLE " + TABLE_PRODUCTS + "("
                    + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "
                    + COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                    + COLUMN_PRODUCT_COST + " REAL NOT NULL, "
                    + COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                    + COLUMN_PRODUCT_IMAGE + " BLOB, "
                    + "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES "
                    + CategoryDatabase.TABLE_CATEGORIES + "(" + CategoryDatabase.COLUMN_CATEGORY_ID + "))";

    public ProductDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create products table
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // Recreate table
        onCreate(db);
    }
}