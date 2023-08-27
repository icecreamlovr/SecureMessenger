package com.icecreamlovr.securemessenger.server.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder encoder;

    // Check if email already exists
    public boolean isEmailInUse(String email) {
        String query = String.format("SELECT * from users where email = '%s'", email);
        List<String> result = jdbcTemplate.query(
                query,
                (rs, rowNum) -> rs.getString("email")
        );
        return !result.isEmpty();
    }

    // Check if email already exists
    public boolean isUsernameInUse(String username) {
        String query = String.format("SELECT * from users where username = '%s'", username);
        List<String> result = jdbcTemplate.query(
                query,
                (rs, rowNum) -> rs.getString("username")
        );
        return !result.isEmpty();
    }

    // Add email, username, password to users table
    public boolean addUser(String email, String username, String password) {
        String hashedPassword = encoder.encode(password);

        String query = String.format(
                "INSERT INTO users(email, username, password, creation_time) " +
                        "VALUES ('%s', '%s', '%s', CURRENT_TIMESTAMP)",
                email, username, hashedPassword);
        return jdbcTemplate.update(query) > 0;
    }

    public boolean verifyPassword(String email, String password) {
        String query = String.format("SELECT * from users where email = '%s'", email);
        List<String> result = jdbcTemplate.query(
                query,
                (rs, rowNum) -> rs.getString("password")
        );
        if (result.size() != 1) {
            System.out.println(">>>verifyPassword got " + result.size() + " results");
            return false;
        }
        return encoder.matches(password, result.get(0));
    }
}
