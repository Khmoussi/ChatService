package com.example.messenger.POJO.Request;

import com.example.messenger.POJO.Enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatroomMessageRequest {
    private String content;
    private long roomId;
    private MessageType messageType;
    private  String contentType;

}
