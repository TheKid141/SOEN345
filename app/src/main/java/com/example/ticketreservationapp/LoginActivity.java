package com.example.ticketreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView goToRegisterText;
    private RadioButton radioAdmin;
    private InputValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        validator = new InputValidator();

        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        loginButton = findViewById(R.id.loginButton);
        goToRegisterText = findViewById(R.id.goToRegisterTextView);
        radioAdmin = findViewById(R.id.radioAdmin);

        loginButton.setOnClickListener(v -> attemptLogin());

        goToRegisterText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        String inputId = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        boolean isAdminChecked = radioAdmin != null && radioAdmin.isChecked();

        // --- ADMIN FLOW (Hardcoded) ---
        if (isAdminChecked) {
            if (inputId.equals("admin") && password.equals("admin123")) {
                Snackbar.make(findViewById(android.R.id.content), "Admin Login Successful", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, EventListActivity.class);
                intent.putExtra("IS_ADMIN", true); // Grant Admin Privileges
                startActivity(intent);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Invalid Admin Credentials", Snackbar.LENGTH_LONG).show();
            }
            return; // Stop here, do not attempt Firebase login
        }

        // --- CUSTOMER FLOW (Firebase Auth) ---

        // Validate inputs for Firebase standard users
        String emailError = validator.validateEmail(inputId);
        if (emailError != null) {
            emailEditText.setError(emailError);
            return;
        }

        String passwordError = validator.validatePassword(password);
        if (passwordError != null) {
            passwordEditText.setError(passwordError);
            return;
        }

        mAuth.signInWithEmailAndPassword(inputId, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Snackbar.make(findViewById(android.R.id.content), "Login Successful", Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, EventListActivity.class);
                        intent.putExtra("IS_ADMIN", false); // Strictly deny Admin Privileges
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Login Failed.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}