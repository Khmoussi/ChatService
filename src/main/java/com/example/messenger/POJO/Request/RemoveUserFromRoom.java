package com.example.messenger.POJO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor

public class RemoveUserFromRoom {
    private String removedUserId;
    private long chatRoomId;
}

