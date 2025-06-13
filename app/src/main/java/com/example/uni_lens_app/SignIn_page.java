package com.example.uni_lens_app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn_page extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance( );
    private EditText SignInEmail, SignInPassword;
    private Button SignInButton;
    private TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        // Bind views
        SignInEmail = findViewById(R.id.username);
        SignInPassword = findViewById(R.id.password);
        SignInButton = findViewById(R.id.signinButton);
        signupRedirectText = findViewById(R.id.signinText);

        // Login button click
        SignInButton.setOnClickListener(v -> {
            String email = SignInEmail.getText().toString().trim();
            String pass = SignInPassword.getText().toString().trim();

            // Validations
            if (email.isEmpty()) {
                SignInEmail.setError("Email cannot be empty");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                SignInEmail.setError("Enter a valid email");
                return;
            }

            if (pass.isEmpty()) {
                SignInPassword.setError("Password cannot be empty");
                return;
            }
            // Firebase authentication
            auth.signInWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(SignIn_page.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignIn_page.this, News_screen.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SignIn_page.this, "Login Failed! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        // Redirect to SignUp
        signupRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(SignIn_page.this, SignUp_page.class));
        });
    }
}