package com.example.messenger.POJO.Response;

import com.example.messenger.POJO.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
public class ChatroomResponse {

    private long roomId;

    private String adminId;
    private String name;
    private ArrayList<ChatroomMessageResponse> messageList;

    public ChatroomResponse(long roomId, String adminId, String name) {
        this.roomId = roomId;
        this.adminId = adminId;
        this.name = name;
        this.messageList= new ArrayList<ChatroomMessageResponse>();
        this.messageList.add(new ChatroomMessageResponse("message from server"));
    }

    public ChatroomResponse() {
    }

    public ChatroomResponse(long roomId, String adminId, String name, ArrayList<ChatroomMessageResponse> messageList) {
        this.roomId = roomId;
        this.adminId = adminId;
        this.name = name;
        this.messageList = messageList;
    }
}
