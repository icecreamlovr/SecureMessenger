package com.icecreamlovr.securemessenger.server;

import com.icecreamlovr.securemessenger.server.models.MessageRequest;
import com.icecreamlovr.securemessenger.server.models.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

public class MessengerController {

    @GetMapping("/messenger")
    public String index() {
        return "index";
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
