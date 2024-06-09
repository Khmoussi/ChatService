package com.example.messenger.POJO.Response;

import com.example.messenger.POJO.ChatRoom;
import com.example.messenger.POJO.ChatroomMessage;
import lombok.Data;

import java.util.List;

@Data
public class ChatroomResponseWithUsers {
    private long id;
    private String roomName;
    private String admin;
    private List<ChatroomMessage> chatroomMessages;
    private List<UserResponse> userResponses;
    public ChatroomResponseWithUsers(ChatRoom chatRoom, List<UserResponse> list){

        this.id= chatRoom.getId();
        this.roomName=chatRoom.getRoomName();
        this.admin=chatRoom.getAdmin().getEmail();
        this.chatroomMessages=chatRoom.getChatroomMessages();
        this.userResponses=list;
    }

}
