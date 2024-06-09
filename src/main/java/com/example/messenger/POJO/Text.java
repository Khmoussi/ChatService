package com.example.messenger.POJO;

import com.example.messenger.POJO.Enums.MessageType;
import jakarta.persistence.Entity;

import java.sql.Timestamp;

@Entity

public class Text extends Message{
    private String message;
    public Text() {
        // Default constructor
    }
    public Text(MessageType type, Timestamp sendDate, String message) {
        super( type, sendDate);
        this.message=message;
    }

    public Text(User sender, User receiver, MessageType type, Timestamp sendDate, String message) {
        super(sender, receiver, type, sendDate);
        this.message = message;
    }
    public Text(User sender, User receiver, MessageType type, Timestamp sendDate, String message,String contentType) {
        super(sender, receiver, type, sendDate,contentType);
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
