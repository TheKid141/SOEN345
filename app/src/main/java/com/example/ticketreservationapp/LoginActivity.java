package com.example.ticketreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView goToRegisterText;
    private RadioButton radioAdmin;

    // Declaring the validator
    private LoginValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Initializing the validator
        validator = new LoginValidator();

        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);
        goToRegisterText = findViewById(R.id.goToRegisterTextView);
        radioAdmin = findViewById(R.id.radioAdmin);

        // Handle Login attempt
        loginButton.setOnClickListener(v -> attemptLogin());

        // Handle navigation to Register screen
        goToRegisterText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        // Using the validator for the role check
        //TODO This check needs to be removed and updated when Admin is added.
        if (!validator.isRoleSelectionValid(radioAdmin.isChecked())) {
            Toast.makeText(this, "Admin portal is under construction.", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Using the validator for the email input check
        String emailError = validator.EmailValidator(email);
        if (emailError != null) {
            emailEditText.setError(emailError);
            return;
        }

        // Using validator for password input check
        String passwordError = validator.PasswordValidator(password);
        if (passwordError != null) {
            passwordEditText.setError(passwordError);
            return;
        }


        // Firebase Sign In
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        // Send to under construction Event List page
                        Intent intent = new Intent(LoginActivity.this, EventListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}