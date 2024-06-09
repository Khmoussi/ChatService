package com.example.messenger.POJO.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteRoomMessageRequest {
   private long roomId;
    private long messageId;
}
