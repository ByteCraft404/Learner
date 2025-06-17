package com.example.codelearner.Models;

public class LoginRequest {
    private String email;
    private String password;

    // No-argument constructor (required for deserialization)
    public LoginRequest() {}

    // Constructor with all fields
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
