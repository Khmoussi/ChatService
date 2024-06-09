package com.example.messenger.POJO;

import com.example.messenger.Config.MessageUserSerializer;
import com.example.messenger.POJO.Enums.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data

public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idMessage;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonSerialize(using = MessageUserSerializer.class)
    @JsonProperty("senderId")
    User sender;
    @ManyToOne
    @JsonSerialize(using = MessageUserSerializer.class)
    @JsonProperty("receiverId")
    @JoinColumn(name = "receiver_id")
    User receiver;


    MessageType type;
    private Timestamp sendDate;
    private String contentType;
    public Message() {
        // Default constructor
    }

    public Message( MessageType type, Timestamp sendDate) {

        this.type = type;
        this.sendDate = sendDate;

    }
    public Message(User sender,User receiver, MessageType type, Timestamp sendDate) {
        this.sender=sender;
        this.receiver=receiver;
        this.type = type;
        this.sendDate = sendDate;
    }
    public Message(User sender,User receiver, MessageType type, Timestamp sendDate,String contentType) {
        this.sender=sender;
        this.receiver=receiver;
        this.type = type;
        this.sendDate = sendDate;
        this.contentType=contentType;
    }

}
