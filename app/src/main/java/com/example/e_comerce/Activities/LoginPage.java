


package com.example.e_comerce.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
///import
import com.example.e_comerce.DatabaseAccess.DbAccsessTransaction;
import com.example.e_comerce.DatabaseAccess.DbAdminAccses;
import com.example.e_comerce.DatabaseAccess.DbCustomerAccses;
import com.example.e_comerce.DatabaseAccess.RememberedUser;
import com.example.e_comerce.DatabaseAccess.RememberedListAccess;
import com.example.e_comerce.JavaClasses.SignIn;
import com.example.e_comerce.JavaClasses.User;
import com.example.e_comerce.JavaClasses.transaction;
import com.example.e_comerce.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;

public class LoginPage extends AppCompatActivity {

    private List<RememberedUser> RememberUserList;
    private List<String> Emails;

    AutoCompleteTextView EmailEditText;
    EditText passwordEditText;
    Button loginButton;
    RadioGroup userTypeRadioGroup;
    SignIn signIn;
    boolean isAuthenticated;
    String Email;
    String password;
    User loggedInUser;
    RememberedListAccess rememberedUserManager;
    MaterialCheckBox rememberMeCheckbox;
    TextView signUpText;
    int selectedRadioId;
    TextView ForgetPasswordText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       // DbAccsessTransaction dbAccsessTransaction=new DbAccsessTransaction(this);
       // long x =dbAccsessTransaction.addTransaction(2,18,"2024-9-9",1);
        //Initialize credentials
        initializeRememberList();
        initializeUIComponents();
        //log in
        loginButton.setOnClickListener(view -> handleLogin());




        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPage.this, SignUp.class);
            startActivity(intent);
        });

        ForgetPasswordText.setOnClickListener(v -> {

            Intent intent = new Intent(LoginPage.this, ForgetPassword.class);
            startActivity(intent);
        });


    }




    private void initializeRememberList() {
        // Initialize the list of credentials
        rememberedUserManager=new RememberedListAccess(this);
        RememberUserList =rememberedUserManager.GetRememberedUsers();
        // Populate usernames list
        if(RememberUserList!=null)
        {
        Emails = new ArrayList<>();

        for (RememberedUser User : RememberUserList) {
            Emails.add(User.Email);
        }
        }
    }


    private void initializeUIComponents() {
        EmailEditText = findViewById(R.id.EmailmEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        userTypeRadioGroup = findViewById(R.id.userTypeRadioGroup);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);
         signUpText = findViewById(R.id.signUpText);
        ForgetPasswordText=findViewById(R.id.forgetPasswordText);


        // Set up AutoComplete for usernames
        ArrayAdapter<String> usernameAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                Emails
        );
        EmailEditText.setAdapter(usernameAdapter);
        EmailEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        // Set up listener for username selection
        EmailEditText.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected username
            String selectedUsername = (String) parent.getItemAtPosition(position);
            // Find and set the corresponding password
            for (RememberedUser User : RememberUserList) {
                if (User.Email.equals(selectedUsername)) {
                    passwordEditText.setText(User.Password);
                    break;
                }
            }
        });
    }




    private void handleLogin() {
        // Get input text from the fields
        Email = EmailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        // Check if both fields are filled
        if (!Email.isEmpty() && !password.isEmpty() ) {
             selectedRadioId= userTypeRadioGroup.getCheckedRadioButtonId();
             if (selectedRadioId == -1)
                 Toast.makeText(LoginPage.this, "Please select a user type", Toast.LENGTH_SHORT).show();
            else {
             // Initialize SignIn based on selected user type
            if (selectedRadioId == R.id.CustomerRadioButton) {
                signIn = new SignIn(new DbCustomerAccses(this));
            } else {
                signIn = new SignIn(new DbAdminAccses(this));
            }
            SigningIn(signIn,selectedRadioId);}
        } else {
            // Show toast message if one or both fields are empty
            Toast.makeText(LoginPage.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
        }

    }

    private void SigningIn(SignIn signIn, int selectedRadioId) {

        // Authenticate the user
        loggedInUser = signIn.Authenticate(Email, password);
        if (loggedInUser != null) {
            // User authentication successful
            HandleRememberMe();
            navigateToHomePage(selectedRadioId);
        }else
            Toast.makeText(LoginPage.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }

    private void HandleRememberMe() {
        if (rememberMeCheckbox.isChecked()) {
            // Push the user to be remembered in future
            rememberedUserManager.AddRememberedUser(loggedInUser);
            Toast.makeText(LoginPage.this, "You will be remembered!", Toast.LENGTH_SHORT).show();
        }
    }


    private void navigateToHomePage(int selectedId) {
        Intent intent;
        if (selectedId == R.id.CustomerRadioButton) {
            intent = new Intent(this, CustomerHomePage.class);
            intent.putExtra("loggedInUser", loggedInUser);
        } else {
            intent = new Intent(this, AdminHomePage.class);
            intent.putExtra("loggedInUser", loggedInUser);
        }
        startActivity(intent);
    }
}

