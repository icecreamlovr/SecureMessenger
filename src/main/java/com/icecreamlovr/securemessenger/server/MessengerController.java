package com.icecreamlovr.securemessenger.server;

import com.icecreamlovr.securemessenger.server.models.MessageRequest;
import com.icecreamlovr.securemessenger.server.models.MessageResponse;
import com.icecreamlovr.securemessenger.server.models.SignupRequest;
import com.icecreamlovr.securemessenger.server.authentication.PasswordHash;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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

@Controller
public class MessengerController {

    @Autowired
    private ApplicationContext applicationContext;


    @GetMapping("/signup")
    public String registrationPage() {
        return "registration";
    }

    private static ConcurrentHashMap<String, List<String>> users = new ConcurrentHashMap<>();

    @PostMapping(
            value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String register(@RequestBody SignupRequest request) {
        // There will still be race condition, because check and set are not atomic
        if (users.containsKey(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        users.put(request.getEmail(), List.of(request.getUsername(), request.getPassword()));
        return "success";
    }

    @GetMapping("/signup-test")
    @ResponseBody
    public ConcurrentHashMap<String, List<String>> registerTest() {
        return users;
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
