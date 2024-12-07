package com.example.e_comerce.DatabaseAccess.AccessingDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_comerce.DatabaseAccess.CreatingDataBase.AdminDatabase;
import com.example.e_comerce.JavaClasses.Admin;

public class DbAccsesAdmin extends DbAccsesUser {

    private final AdminDatabase dbHelper;
    private DbAccsesCustomer cutomerDbAccess;
    Context context;

    public DbAccsesAdmin(Context context) {
        dbHelper = new AdminDatabase(context);
        this.context=context;
       // Use AdminDatabase as helper class
    }

    // Method to insert a new admin
    public boolean registerAdmin(String username, String email, String password) {
        cutomerDbAccess=new DbAccsesCustomer(context);
        if(cutomerDbAccess.CheckUserExists(email))
            return false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AdminDatabase.COLUMN_USERNAME, username);
        values.put(AdminDatabase.COLUMN_EMAIL, email);
        values.put(AdminDatabase.COLUMN_PASSWORD, password);

        long result = db.insert(AdminDatabase.TABLE_ADMINS, null, values);
        db.close();
        return result != -1; // Return true if the insert was successful
    }

    public boolean CheckUserExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to check for admin using email
        String query = "SELECT * FROM " + AdminDatabase.TABLE_ADMINS +
                " WHERE " + AdminDatabase.COLUMN_EMAIL + " = ?";

        // Execute the query with the provided email
        Cursor cursor = db.rawQuery(query, new String[]{email});

        // Check if a matching record exists
        boolean exists = cursor.moveToFirst();

        // Close the cursor and database
        cursor.close();
        db.close();

        return exists;  // Return true if admin with email exists, false otherwise
    }
    // Method to check if an admin exists by username and password
    public Admin CheckUserExists(String email, String password) {
       // DeleteAllRememberedUsers();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to check for admin using email and password
        String query = "SELECT * FROM " + AdminDatabase.TABLE_ADMINS +
                " WHERE " + AdminDatabase.COLUMN_EMAIL + " = ? AND " +
                AdminDatabase.COLUMN_PASSWORD + " = ?";

        // Execute the query with the provided email and password
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        Admin admin = null;

        // If an admin with the provided credentials is found, create the Admin object
        if (cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(AdminDatabase.COLUMN_USERNAME));

            // Create Admin object with the retrieved data
            admin = new Admin(username, password, email);
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return admin;  // Return null if no admin found
    }

    public boolean updatePassword(String userEmail, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Prepare the content values with new password
        ContentValues values = new ContentValues();
        values.put(AdminDatabase.COLUMN_PASSWORD, newPassword);

        // Update the password where email matches
        int rowsAffected = db.update(
                AdminDatabase.TABLE_ADMINS,  // Table name
                values,                      // New values to update
                AdminDatabase.COLUMN_EMAIL + " = ?",  // Where clause
                new String[]{userEmail}      // Where arguments
        );

        // Close the database
        db.close();

        // Return true if at least one row was updated
        return rowsAffected > 0;
    }


  /*  public void DeleteAllRememberedUsers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(AdminDatabase.TABLE_ADMINS, null, null); // Deletes all rows
        db.close();
    }*/

}
