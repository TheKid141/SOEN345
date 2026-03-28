package com.example.ticketreservationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private TextView backToLoginButton;
    private InputValidator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        validator = new InputValidator();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerEmailButton);
        backToLoginButton = findViewById(R.id.btnBackToLogin);

        // Close screen and go back to Login
        backToLoginButton.setOnClickListener(v -> finish());

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        String emailError = validator.validateEmail(email);
        if (emailError != null) {
            emailEditText.setError(emailError);
            return;
        }

        String passwordError = validator.validatePassword(password);
        if (passwordError != null) {
            passwordEditText.setError(passwordError);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Snackbar.make(findViewById(android.R.id.content), "Registration Successful.", Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, EventListActivity.class);
                        // Force the user to be a customer, preventing privilege escalation
                        intent.putExtra("IS_ADMIN", false);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "Registration Failed.", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}