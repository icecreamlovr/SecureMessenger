package com.icecreamlovr.securemessenger.server.models;

public class MessageResponse {
    private final boolean delivered;

    public MessageResponse(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean getDelivered() {
        return delivered;
    }
}
