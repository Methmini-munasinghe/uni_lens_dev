package com.example.uni_lens_app;

public class User {
    public String name;
    public String email;
    public String password;
    public String confirmPassword;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

