package com.example.e_comerce.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.DatabaseAccess.AccessingDataBase.DbAccsesAdmin;
import com.example.e_comerce.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AdminSignUp extends AppCompatActivity {
    private TextInputEditText etUsername;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    private MaterialButton btnSignUp;
    private DbAccsesAdmin adminDbAccess;
    String username, password, birthdate, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupSignUpButton();
    }
    private void initializeViews() {
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        btnSignUp = findViewById(R.id.btn_signup);
    }

    private void setupSignUpButton() {
        btnSignUp.setOnClickListener(v -> {
            if (validateForm()) {

               adminDbAccess  = new DbAccsesAdmin(this);
                boolean registrationSuccess = adminDbAccess.registerAdmin(username, email, password);

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


        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}