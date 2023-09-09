package com.icecreamlovr.securemessenger.server.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    // TODO(icecreamlovr): Change to read from external configuration.
    private static final String SECRET = "my-super-duper-secret-secret";    // Not base64 encoded
    private static final Algorithm HASH_ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final JWTVerifier JWT_VERIFIER = JWT.require(HASH_ALGORITHM).build();

    // 5 hours.
    private static final long JWT_TOKEN_VALIDITY_MS = 5 * 60 * 60 * 1000;

    /** Generate token with expiration date. */
    public String generate(String email) {
        Date issuedDate = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_MS);
        return JWT.create()
                .withClaim("email", email)
                .withIssuedAt(issuedDate)
                .withExpiresAt(expiryDate)
                .sign(HASH_ALGORITHM);
    }

    /**
     * Validates token signature and expiry.
     * If the token is invalid, rethrow IllegalArgumentException exception.
     * If the token is valid, decode and get the email from it.
     */
    public String verifyAndGetEmail(String token) {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT_VERIFIER.verify(token);
        } catch (JWTVerificationException e) {
            // TODO(icecreamlovr): Handle subclasses of JWTVerificationException
            System.out.println("[WARN] JWT verify failed. Exception: " + e.getMessage());
            throw new IllegalArgumentException("Invalid token");
        }

        if (!decodedJWT.getClaims().containsKey("email")) {
            System.out.println("[WARN] JWT verify failed. JWT doesn't contain email");
            throw new IllegalArgumentException("Invalid token");
        }

        return decodedJWT.getClaim("email").asString();
    }
}
