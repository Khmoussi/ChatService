package com.example.messenger.POJO.Response;

import com.example.messenger.POJO.Enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
@Data
@AllArgsConstructor

public class ChatroomMessageResponse {
    private long idMessage;
    private String Content;
    private Date sendDate;
    private MessageType messageType;

    private String senderId;
    private long roomId;
    private UserResponse userResponse;
    private String contentType;

    public ChatroomMessageResponse(String content, Date sendDate, MessageType messageType, String senderId, long roomId) {
        Content = content;
        this.sendDate = sendDate;
        this.messageType = messageType;
        this.senderId = senderId;
        this.roomId = roomId;
    }
    public ChatroomMessageResponse(String content, Date sendDate, MessageType messageType, String senderId, long roomId,String contentType) {
        Content = content;
        this.sendDate = sendDate;
        this.messageType = messageType;
        this.senderId = senderId;
        this.roomId = roomId;
        this.contentType=contentType;
    }
    public ChatroomMessageResponse(long idMessage,String content, Date sendDate, MessageType messageType, String senderId, long roomId) {
        this.idMessage=idMessage;
        this.Content = content;
        this.sendDate = sendDate;
        this.messageType = messageType;
        this.senderId = senderId;
        this.roomId = roomId;
    }
    public ChatroomMessageResponse(String content, Date sendDate, MessageType messageType, String senderId, long roomId,UserResponse userResponse) {
        Content = content;
        this.sendDate = sendDate;
        this.messageType = messageType;
        this.senderId = senderId;
        this.roomId = roomId;
        this.userResponse=userResponse;
    }

    public ChatroomMessageResponse(String content) {
        Content = content;
    }
}
