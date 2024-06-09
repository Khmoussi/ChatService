package com.example.messenger.POJO;

import com.example.messenger.Config.MessageUserSerializer;
import com.example.messenger.POJO.Enums.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
public class ChatroomMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idMessage;
    private String Content;
    private Timestamp sendDate;
    private MessageType messageType;
    private String contentType ="text";
    @JsonSerialize(using = MessageUserSerializer.class)
    @JsonProperty("senderId")

    @ManyToOne

    @JoinColumn(name = "sender_id")
    private User user;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;


    public ChatroomMessage(){}

    public ChatroomMessage(String content,  Timestamp sendDate, MessageType messageType) {
        Content = content;

        this.sendDate = sendDate;
        this.messageType = messageType;
    }
    public ChatroomMessage(String content,  Timestamp sendDate, MessageType messageType,User user,ChatRoom chatRoom ,String contentType) {
        Content = content;
        this.contentType =contentType;
        this.sendDate = sendDate;
        this.messageType = messageType;
        this.chatRoom=chatRoom;
        this.user=user;
    }
    public ChatroomMessage(String content,  Timestamp sendDate, MessageType messageType,User user,ChatRoom chatRoom ) {
        Content = content;
        this.contentType =contentType;
        this.sendDate = sendDate;
        this.messageType = messageType;
        this.chatRoom=chatRoom;
        this.user=user;
    }


}
