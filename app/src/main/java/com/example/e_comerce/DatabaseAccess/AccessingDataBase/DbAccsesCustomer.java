package com.example.e_comerce.DatabaseAccess.AccessingDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_comerce.DatabaseAccess.CreatingDataBase.CustomerDatabase;
import com.example.e_comerce.JavaClasses.Customer;
import com.example.e_comerce.JavaClasses.date;
import com.example.e_comerce.JavaClasses.User;

public class DbAccsesCustomer extends DbAccsesUser {
    CustomerDatabase dbHelper;
    DbAccsesAdmin adminDbAccess;
    public DbAccsesCustomer(Context context) {
        this.dbHelper = new CustomerDatabase(context);
        adminDbAccess=new DbAccsesAdmin(context);
    }

    // Register a new customer
    public boolean registerCustomer(String username, String email, String password, String birthdate) {

        if (adminDbAccess.CheckUserExists(email))
            return  false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CustomerDatabase.COLUMN_USERNAME, username);
        values.put(CustomerDatabase.COLUMN_EMAIL, email);
        values.put(CustomerDatabase.COLUMN_PASSWORD, password);
        values.put(CustomerDatabase.COLUMN_BIRTHDATE, birthdate);

        long result = db.insert(CustomerDatabase.TABLE_CUSTOMERS, null, values);
        db.close();

        // Return true if insertion was successful, false otherwise
        return result != -1;
    }
    // Check if user exists
    public boolean CheckUserExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to check for admin using email
        String query = "SELECT * FROM " + CustomerDatabase.TABLE_CUSTOMERS +
                " WHERE " + CustomerDatabase.COLUMN_EMAIL + " = ?";

        // Execute the query with the provided email
        Cursor cursor = db.rawQuery(query, new String[]{email});

        // Check if a matching record exists
        boolean exists = cursor.moveToFirst();

        // Close the cursor and database
        cursor.close();
        db.close();

        return exists;  // Return true if admin with email exists, false otherwise
    }
    public User CheckUserExists(String email, String password) {
       //DeleteAllRememberedUsers();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + CustomerDatabase.TABLE_CUSTOMERS +
                " WHERE " + CustomerDatabase.COLUMN_EMAIL + " = ? AND " +
                CustomerDatabase.COLUMN_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        Customer customer = null;

        if (cursor.moveToFirst()) {
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
        }

        cursor.close();
        db.close();

        return customer;  // Returns null if user does not exist
    }

    public boolean updatePassword(String userEmail, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Prepare the content values with new password
        ContentValues values = new ContentValues();
        values.put(CustomerDatabase.COLUMN_PASSWORD, newPassword);

        // Update the password where email matches
        int rowsAffected = db.update(
                CustomerDatabase.TABLE_CUSTOMERS,  // Table name
                values,                            // New values to update
                CustomerDatabase.COLUMN_EMAIL + " = ?",  // Where clause
                new String[]{userEmail}            // Where arguments
        );

        // Close the database
        db.close();

        // Return true if at least one row was updated
        return rowsAffected > 0;
    }


  /* public void DeleteAllRememberedUsers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CustomerDatabase.TABLE_CUSTOMERS, null, null); // Deletes all rows
        db.close();
    }*/


}
