package com.example.uni_lens_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.example.uni_lens_app.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class News_screen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_screen);

        // Initialize Drawer Layout and Navigation View
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Setup hamburger icon
        findViewById(R.id.menu_icon).setOnClickListener(v ->
                drawerLayout.openDrawer(GravityCompat.START));

        TextView Description_01 = findViewById(R.id.newsTitle1);
        TextView Date_01 = findViewById(R.id.newsDate1);
        ImageView Image_01 = findViewById(R.id.newsImage1);

        readData("sports","A", false, Description_01, Date_01, Image_01);

        TextView Description_02 = findViewById(R.id.newsTitle2);
        TextView Date_02 = findViewById(R.id.newsDate2);
        ImageView Image_02 = findViewById(R.id.newsImage2);

        readData("sports","B", false, Description_02, Date_02, Image_02);

        ImageButton academic_btn = findViewById(R.id.btn_academic);
        academic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData("academic","A", true,Description_01, Date_01, Image_01);
                readData("academic","B", true,Description_02, Date_02, Image_02);
            }
        });

        ImageButton sports_btn = findViewById(R.id.btn_sports);
        sports_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData("sports","A", true,Description_01, Date_01, Image_01);
                readData("sports","B", true,Description_02, Date_02, Image_02);
            }
        });

        ImageButton events_btn = findViewById(R.id.btn_events);
        events_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData("events","A", true, Description_01, Date_01, Image_01);
                readData("events","B", true,Description_02, Date_02, Image_02);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation item clicks
        int id = item.getItemId();

        if (id == R.id.nav_user_info) {
            Intent intent = new Intent(News_screen.this, User_info.class);
            startActivity(intent);
        } else if (id == R.id.nav_developer_info) {
            Intent intent = new Intent(News_screen.this, developer_screen.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void readData(String event_base_key, String event_sub_ey, boolean val_chk,
                          TextView out_description, TextView out_data, ImageView out_image
    ) {
        TextView top_header = findViewById(R.id.top_header);

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Images")
                .child(event_base_key)
                .child(event_sub_ey);

        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();

                if (dataSnapshot.exists()) {
                    String header = dataSnapshot.child("header").getValue(String.class);
                    String imageUrl = dataSnapshot.child("image").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String date = dataSnapshot.child("date").getValue(String.class);

                    // Use the data (example: log or Toast)
                    Log.d("FirebaseData", "Header: " + header);
                    Log.d("FirebaseData", "Image: " + imageUrl);
                    Log.d("FirebaseData", "Description: " + description);
                    Log.d("FirebaseData", "Date: " + date);

                    if (val_chk)
                    {
                        top_header.setText(header);
                    }
                    out_description.setText(description);
                    out_data.setText(date);
                    Glide.with(this)
                            .load(imageUrl)
                            .into(out_image);

                } else {
                    Toast.makeText(News_screen.this, "Event not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(News_screen.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
