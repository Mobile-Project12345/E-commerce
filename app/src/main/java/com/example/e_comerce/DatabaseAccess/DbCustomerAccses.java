package com.example.e_comerce.DatabaseAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_comerce.JavaClasses.Customer;
import com.example.e_comerce.JavaClasses.date;
import com.example.e_comerce.JavaClasses.User;

public class DbCustomerAccses extends DbUserAccses {
    CustomerDatabase dbHelper;
    DbAdminAccses adminDbAccess;

    public DbCustomerAccses(Context context) {
        this.dbHelper = new CustomerDatabase(context);
        adminDbAccess = new DbAdminAccses(context);
    }

    // New method to get Customer by ID
    public Customer getCustomerById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Customer customer = null;

        // Query to find customer by ID
        String query = "SELECT * FROM " + CustomerDatabase.TABLE_CUSTOMERS +
                " WHERE " + CustomerDatabase.COLUMN_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            // Retrieve column indices
            int usernameIndex = cursor.getColumnIndexOrThrow(CustomerDatabase.COLUMN_USERNAME);
            int emailIndex = cursor.getColumnIndexOrThrow(CustomerDatabase.COLUMN_EMAIL);
            int passwordIndex = cursor.getColumnIndexOrThrow(CustomerDatabase.COLUMN_PASSWORD);
            int birthdateIndex = cursor.getColumnIndexOrThrow(CustomerDatabase.COLUMN_BIRTHDATE);

            // Extract data from cursor
            String username = cursor.getString(usernameIndex);
            String email = cursor.getString(emailIndex);
            String password = cursor.getString(passwordIndex);
            String birthdateStr = cursor.getString(birthdateIndex);

            // Parse birthdate string into day, month, and year
            String[] dateParts = birthdateStr.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            // Create the date object
            date birthdate = new date(day, month, year);

            // Create and populate Customer object
            customer = new Customer(username, email, password, birthdate);
        }

        // Close cursor and database
        cursor.close();
        db.close();

        return customer;  // Returns null if no customer found with the given ID
    }

    // Existing methods remain the same...
    public boolean registerCustomer(String username, String email, String password, String birthdate) {
        if (adminDbAccess.CheckUserExists(email))
            return false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CustomerDatabase.COLUMN_USERNAME, username);
        values.put(CustomerDatabase.COLUMN_EMAIL, email);
        values.put(CustomerDatabase.COLUMN_PASSWORD, password);
        values.put(CustomerDatabase.COLUMN_BIRTHDATE, birthdate);

        long result = db.insert(CustomerDatabase.TABLE_CUSTOMERS, null, values);
        db.close();

        return result != -1;
    }

    public boolean CheckUserExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + CustomerDatabase.TABLE_CUSTOMERS +
                " WHERE " + CustomerDatabase.COLUMN_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }

    public User CheckUserExists(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + CustomerDatabase.TABLE_CUSTOMERS +
                " WHERE " + CustomerDatabase.COLUMN_EMAIL + " = ? AND " +
                CustomerDatabase.COLUMN_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        Customer customer = null;

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(CustomerDatabase.COLUMN_ID));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(CustomerDatabase.COLUMN_USERNAME));
            String birthdateStr = cursor.getString(cursor.getColumnIndexOrThrow(CustomerDatabase.COLUMN_BIRTHDATE));

            // Parse birthdate string into day, month, and year
            String[] dateParts = birthdateStr.split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);

            // Create the date object
            date birthdate = new date(day, month, year);

            customer = new Customer(username, email, password, birthdate);
            customer.id = userId;
            // Set the actual user ID from the database
        }

        cursor.close(); // Important: close the cursor to free up resources
        db.close(); // Close the database connection

        return customer;
    }

    public boolean updatePassword(String userEmail, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CustomerDatabase.COLUMN_PASSWORD, newPassword);

        int rowsAffected = db.update(
                CustomerDatabase.TABLE_CUSTOMERS,
                values,
                CustomerDatabase.COLUMN_EMAIL + " = ?",
                new String[]{userEmail}
        );

        db.close();

        return rowsAffected > 0;
    }
}