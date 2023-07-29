package com.icecreamlovr.securemessenger.server.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHash {
    @Autowired
    private PasswordEncoder encoder;

    public String getHash(String password) {
        return encoder.encode(password);
    }

    public boolean verify(String password, String hash) {
        return encoder.matches(password, hash);
    }

    public String verifySingleton() {
        return encoder.toString();
    }
}
