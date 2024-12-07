package com.example.e_comerce.DatabaseAccess.AccessingDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.e_comerce.DatabaseAccess.CreatingDataBase.RememberMeDataBase;
import com.example.e_comerce.JavaClasses.User;

import java.util.ArrayList;
import java.util.List;

public class DbAccessRememberedList {

    private final RememberMeDataBase dbHelper;
    List<RememberedUser> rememberedUsers;

    public DbAccessRememberedList(Context context) {

        dbHelper = new RememberMeDataBase(context);
        rememberedUsers=new ArrayList<>();
    // Use AdminDatabase as helper class

    }


    public List<RememberedUser> GetRememberedUsers() {
      // DeleteAllRememberedUsers();
        rememberedUsers.clear(); // Clear any existing users

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query to select all remembered users
        Cursor cursor = db.query(
                RememberMeDataBase.TABLE_REMEMBERME,
                new String[]{RememberMeDataBase.COLUMN_EMAIL, RememberMeDataBase.COLUMN_PASSWORD},
                null,
                null,
                null,
                null,
                null
        );

        // Check if there are any users in the database
        if (cursor.moveToFirst()) {
            do {
                // Get email and password from cursor
                String email = cursor.getString(cursor.getColumnIndexOrThrow(RememberMeDataBase.COLUMN_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(RememberMeDataBase.COLUMN_PASSWORD));

                // Add user to the list
                rememberedUsers.add(new RememberedUser(email, password));
            } while (cursor.moveToNext());
        } else {
            // If no users found, add default message
            rememberedUsers.add(new RememberedUser("No remembered users yet", ""));
        }

        // Close cursor and database
        cursor.close();
        db.close();

        return rememberedUsers;
    }



    public boolean AddRememberedUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // First, check if the user already exists
        Cursor cursor = db.query(
                RememberMeDataBase.TABLE_REMEMBERME,
                new String[]{RememberMeDataBase.COLUMN_EMAIL},
                RememberMeDataBase.COLUMN_EMAIL + " = ?",
                new String[]{user.Email},
                null,
                null,
                null
        );

        boolean userExists = cursor.moveToFirst();
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(RememberMeDataBase.COLUMN_EMAIL, user.Email);
        values.put(RememberMeDataBase.COLUMN_PASSWORD, user.Password);

        long result;
        if (userExists) {
            // If user exists, update the password
            result = db.update(
                    RememberMeDataBase.TABLE_REMEMBERME,
                    values,
                    RememberMeDataBase.COLUMN_EMAIL + " = ?",
                    new String[]{user.Email}
            );
        } else {
            // If user doesn't exist, insert a new record
            result = db.insert(RememberMeDataBase.TABLE_REMEMBERME, null, values);
        }

        db.close();

        return result != -1;
    }

    public void DeleteAllRememberedUsers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(RememberMeDataBase.TABLE_REMEMBERME, null, null); // Deletes all rows
        db.close();
    }

}