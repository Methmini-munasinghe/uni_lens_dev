package com.example.uni_lens_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class User_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton exit_btn = findViewById(R.id.menu_icon);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_info.this, News_screen.class);
                finish();
            }
        });

        // SIGN OUT button logic
        Button signOutButton = findViewById(R.id.signOutButton);
        if (signOutButton != null) {
            signOutButton.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setMessage("Really want to sign out?")
                        .setCancelable(true)
                        .setPositiveButton("OK", (dialog, which) -> {
                            // 🔐 Firebase sign-out
                            FirebaseAuth.getInstance().signOut();

                            // 👇 Optional: Redirect to login screen
                            Intent intent = new Intent(User_info.this, SignIn_page.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                            startActivity(intent);
                            dialog.dismiss();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
            });
        }

        // EDIT button logic
        Button editButton = findViewById(R.id.editButton);
        if (editButton != null) {
            editButton.setOnClickListener(v -> {
                // Inflate custom dialog layout
                LayoutInflater inflater = LayoutInflater.from(this);
                View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);

                EditText etUsername = dialogView.findViewById(R.id.et_username);
                EditText etEmail = dialogView.findViewById(R.id.et_email);

                //  pre-fill with current values
                etUsername.setText("Methmi@2003");
                etEmail.setText("Methmi23@gmail.com");

                AlertDialog editDialog = new AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setCancelable(true)
                        .create();

                Button btnOk = dialogView.findViewById(R.id.btn_ok);
                Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

                btnOk.setOnClickListener(okView -> {
                    //update box logic
                    String newUsername = etUsername.getText().toString();
                    String newEmail = etEmail.getText().toString();

                    editDialog.dismiss();
                });

                btnCancel.setOnClickListener(cancelView -> editDialog.dismiss());

                editDialog.show();
            });
        }
    }
}