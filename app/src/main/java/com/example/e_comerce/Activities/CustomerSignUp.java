package com.example.e_comerce.Activities;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.DatabaseAccess.AccessingDataBase.DbAccsesCustomer;
import com.example.e_comerce.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomerSignUp extends AppCompatActivity {

    private TextInputEditText etUsername;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etBirthdate;
    private MaterialButton btnSignUp;
    private DbAccsesCustomer customerDbAccess;
    String username,password,birthdate,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_sign_up);

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize UI elements
        initializeViews();

        // Setup date picker
        setupDatePicker();

        // Setup sign-up button click listener
        setupSignUpButton();
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etBirthdate = findViewById(R.id.et_birthdate);
        btnSignUp = findViewById(R.id.btn_signup);
    }

    private void setupDatePicker() {
        etBirthdate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        etBirthdate.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void setupSignUpButton() {
        btnSignUp.setOnClickListener(v -> {
            if (validateForm()) {
                customerDbAccess=new DbAccsesCustomer(this);
                boolean registrationSuccess = customerDbAccess.registerCustomer(username, email, password, birthdate);

                if (registrationSuccess) {
                    showToast("Registration Successful!");
                    // Optional: Navigate to login or main screen
                    Intent intent = new Intent(this, LoginPage.class);
                    startActivity(intent);
                } else {
                    showToast("Registration Failed. Username might already exist.");
                }
            }
        });
    }

    private boolean validateForm() {
        // Existing validation logic remains the same
        // (from your previous implementation)
         username = etUsername.getText().toString().trim();
        if (username.isEmpty()) {
            showToast("Please enter a username");
            return false;
        }

         email = etEmail.getText().toString().trim();
        if (email.isEmpty()) {
            showToast("Please enter an email address");
            return false;
        }

         password = etPassword.getText().toString().trim();
        if (password.isEmpty()) {
            showToast("Please enter a password");
            return false;
        }

         birthdate = etBirthdate.getText().toString().trim();
        if (birthdate.isEmpty()) {
            showToast("Please select a date of birth");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}