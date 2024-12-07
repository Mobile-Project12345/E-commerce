package com.example.e_comerce.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_comerce.DatabaseAccess.AccessingDataBase.DbAccsesAdmin;
import com.example.e_comerce.DatabaseAccess.AccessingDataBase.DbAccsesCustomer;
import com.example.e_comerce.JavaClasses.EmailSender;
import com.example.e_comerce.R;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.security.SecureRandom;

public class ForgetPassword extends AppCompatActivity {
    String verificationCode;
    private TextInputEditText etEmail;
    private MaterialButton btnResetPassword;
    private DbAccsesAdmin adminDbAccess;
    private DbAccsesCustomer customerDbAccess;

    boolean  IsAdmin=false;
    boolean IsCustomer=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Edge-to-Edge Setup
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Database Helper
        adminDbAccess = new DbAccsesAdmin(this);

        customerDbAccess=new DbAccsesCustomer(this);


        // Initialize Email Input
        etEmail = findViewById(R.id.etEmail);

        handleResetPassword();
    }

    private void handleResetPassword() {
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            if (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                  SendVerificationCode(email);

            } else {
                Toast.makeText(ForgetPassword.this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void SendVerificationCode(String email) {


        IsAdmin=adminDbAccess.CheckUserExists(email);
        IsCustomer=customerDbAccess.CheckUserExists(email);



        // Check if email exists in database
        if (IsCustomer||IsAdmin)
        {
            // Generate a secure random verification code
             verificationCode = generateVerificationCode();


            // Send email with verification code
            sendPasswordResetEmail(email, verificationCode);
            // Launch Verification Code Activity
            Intent verificationIntent = new Intent(this, VerificationCodeActivity.class);
            verificationIntent.putExtra("VERIFICATION_CODE", verificationCode);
            verificationIntent.putExtra("USER_EMAIL", email);
            startActivity(verificationIntent);
            Toast.makeText(this, "Verification code sent to your email", Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(this, "No account found with this email", Toast.LENGTH_SHORT).show();
        }

    }

    private String generateVerificationCode() {
        // Generate a 6-digit verification code
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    private void sendPasswordResetEmail(String email, String verificationCode) {
        // Send email
        EmailSender.sendVerificationCode(email, verificationCode);
    }
}