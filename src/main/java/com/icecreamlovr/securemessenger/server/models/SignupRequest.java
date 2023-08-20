package com.icecreamlovr.securemessenger.server.models;

public class SignupRequest {

    private final String email;
    private final String username;
    private final String password;

    public SignupRequest(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
