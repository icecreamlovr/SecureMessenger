package com.icecreamlovr.securemessenger.server.models;

public class MessageRequest {
    private final String recipient;
    private final String message;

    public MessageRequest() {
        this.recipient = "";
        this.message = "";
    }

    public MessageRequest(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }
}
