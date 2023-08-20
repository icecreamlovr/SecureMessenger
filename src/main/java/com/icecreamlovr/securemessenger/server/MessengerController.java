package com.icecreamlovr.securemessenger.server;

import com.icecreamlovr.securemessenger.server.models.MessageRequest;
import com.icecreamlovr.securemessenger.server.models.MessageResponse;
import com.icecreamlovr.securemessenger.server.models.SignupRequest;
import com.icecreamlovr.securemessenger.server.authentication.PasswordHash;

import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Controller
public class MessengerController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/signup")
    public String registrationPage() {
        return "registration";
    }

    // Check if email already exists
    private boolean isEmailInUse(String email) {
        String query = String.format("SELECT * from users where email = '%s'", email);
        List<String> result = jdbcTemplate.query(
                query,
                (rs, rowNum) -> rs.getString("email")
            );
        return !result.isEmpty();
    }

    // Check if email already exists
    private boolean isUsernameInUse(String username) {
        String query = String.format("SELECT * from users where username = '%s'", username);
        List<String> result = jdbcTemplate.query(
                query,
                (rs, rowNum) -> rs.getString("username")
        );
        return !result.isEmpty();
    }

    // Add email, username, password to users table
    private boolean addUser(String email, String username, String password) {
        String query = String.format(
                "INSERT INTO users(email, username, password, creation_time) " +
                        "VALUES ('%s', '%s', '%s', CURRENT_TIMESTAMP)",
                email, username, password);
        return jdbcTemplate.update(query) > 0;
    }

    @PostMapping(
            value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String register(@RequestBody SignupRequest request) {
        // There will still be race condition, because check and set are not atomic
        if (isEmailInUse(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }
        if (isUsernameInUse(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name already in use");
        }

        boolean addResult;
        try {
            addResult = addUser(request.getEmail(), request.getUsername(), request.getPassword());
        } catch (Exception ex) {
            System.out.println(">>>Exception!!" + ex.toString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Registration failed");
        }

        if (addResult) {
            return "success";
        } else {
            System.out.println(">>>Not sure what happened");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Not sure what happened");
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/messenger")
    public String index() {
        return "index";
    }

    @GetMapping("/password-test1")
    @ResponseBody
    public String test() {
        return applicationContext.getBean(PasswordHash.class).verifySingleton();
    }

    @GetMapping("/password-test2")
    @ResponseBody
    public String test2() {
        return applicationContext.getBean(PasswordHash.class).verifySingleton();
    }

    @GetMapping("/password-test3")
    @ResponseBody
    public String test3() {
        PasswordHash ph = applicationContext.getBean(PasswordHash.class);
        String hash1 = ph.getHash("mysuperdupersecurepassword");
        String hash2 = ph.getHash("mysuperdupersecurepassword");
        boolean matches = ph.verify("mysuperdupersecurepassword", hash1);
        return String.format("hash1: %s\nhash2: %s\nveridy: %b", hash1, hash2, matches);
    }

    @PostMapping(
            value = "/messenger/message", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public MessageResponse solve(@RequestBody MessageRequest request) {
        String recipient = request.getRecipient();
        String message = request.getMessage();
        System.out.println(
                ">>> Received message request"
                        + ". To:" + recipient
                        + ". Message: " + message);
        if (recipient.equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipient is empty!");
        }
        return new MessageResponse(true);

    }
}
