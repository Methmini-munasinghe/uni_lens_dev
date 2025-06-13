package com.example.uni_lens_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_info extends AppCompatActivity {
    private String uid;
    private String email_user;
    private String username_user;

    @SuppressLint("SetTextI18n")
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            TextView Username = findViewById(R.id.text_user);
            TextView Email = findViewById(R.id.user_email);

            uid = user.getUid();                    // Profile photo URL (can be null)

            DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(uid);

            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();

                    email_user = dataSnapshot.child("email").getValue(String.class);
                    username_user = dataSnapshot.child("name").getValue(String.class);

                    Username.setText("Username :  " + username_user);
                    Email.setText("Email         :  " + email_user);

                }

            });
        } else {
            Log.d("UserInfo", "No user is signed in.");
        }

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
                etUsername.setText(username_user);
                etEmail.setText(email_user);

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

                    // Validations
                    if (newUsername.isEmpty()) {
                        newUsername = username_user;
                    }

                    // Validations
                    if (newEmail.isEmpty()) {
                        newEmail = email_user;
                    }

                    User user_out = new User(newUsername, newEmail);
                    DatabaseReference usersRef = FirebaseDatabase.getInstance()
                            .getReference("Users");
                    usersRef.child(uid).setValue(user_out);

                    editDialog.dismiss();
                    Intent intent = getIntent();
                    finish(); // Finish current activity
                    startActivity(intent); // Restart the same activity
                });

                btnCancel.setOnClickListener(cancelView -> editDialog.dismiss());

                editDialog.show();
            });
        }
    }
}