package com.icecreamlovr.securemessenger.server.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder encoder;

    public boolean verifyLogin(String email, String password) {
        String query = String.format("SELECT * from users where email = '%s'", email);
        List<String> result = jdbcTemplate.query(
                query,
                (rs, rowNum) -> rs.getString("password")
        );
        if (result.size() == 0) {
            // email not exist
            return false;
        }
        if (result.size() > 1) {
            System.out.println(">>>verifyLogin got " + result.size() + " results");
            throw new IllegalStateException(String.format("verifyLogin got more than 1 (%s) results", result.size()));
        }
        return encoder.matches(password, result.get(0));
    }
}
