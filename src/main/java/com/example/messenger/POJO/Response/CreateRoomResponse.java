package com.example.messenger.POJO.Response;

import lombok.Data;

@Data
public class CreateRoomResponse {
    long chatRoomId;

    public CreateRoomResponse(long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
