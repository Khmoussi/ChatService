package com.example.messenger.POJO.Response;

import lombok.Data;

@Data
public class JoinChatroomMessageResponse {
    ChatroomMessageResponse chatroomMessageResponse;
    UserResponse userResponse;

    public JoinChatroomMessageResponse(ChatroomMessageResponse chatroomMessageResponse, UserResponse userResponse) {
        this.chatroomMessageResponse = chatroomMessageResponse;
        this.userResponse = userResponse;
    }
}
