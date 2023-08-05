package com.icecreamlovr.securemessenger.server;

import com.icecreamlovr.securemessenger.server.models.MessageRequest;
import com.icecreamlovr.securemessenger.server.models.MessageResponse;
import com.icecreamlovr.securemessenger.server.authentication.PasswordHash;
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
