package com.icecreamlovr.securemessenger.server;

import com.icecreamlovr.securemessenger.server.authentication.LoginService;
import com.icecreamlovr.securemessenger.server.authentication.RegistrationService;
import com.icecreamlovr.securemessenger.server.models.LoginRequest;
import com.icecreamlovr.securemessenger.server.models.MessageRequest;
import com.icecreamlovr.securemessenger.server.models.MessageResponse;
import com.icecreamlovr.securemessenger.server.models.SignupRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class MessengerController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private LoginService loginService;

    @GetMapping("/signup")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping(
            value = "/signup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String register(@RequestBody SignupRequest request) {
        // There will still be race condition, because check and set are not atomic
        if (registrationService.isEmailInUse(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }
        if (registrationService.isUsernameInUse(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name already in use");
        }

        boolean addResult;
        try {
            addResult = registrationService.addUser(request.getEmail(), request.getUsername(), request.getPassword());
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


    @PostMapping(
            value = "/login", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String login(@RequestBody LoginRequest request) {
        boolean isValidUser = false;
        try {
            isValidUser = loginService.verifyLogin(request.getEmail(), request.getPassword());
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Login failed due to unknown reason");
        }

        if (isValidUser) {
            return "success";
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed");
        }
    }

    @GetMapping("/messenger")
    public String index() {
        return "index";
    }

    @GetMapping("/password-test2")
    @ResponseBody
    public boolean test2() {
        return loginService.verifyLogin("verify-refactor@test.com", "whatever");
    }

    @GetMapping("/password-test3")
    @ResponseBody
    public boolean test3() {
        return loginService.verifyLogin("verify-refactor@test.com", "whatever-wrong");
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
