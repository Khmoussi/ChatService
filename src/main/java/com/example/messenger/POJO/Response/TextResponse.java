package com.example.messenger.POJO.Response;

import com.example.messenger.POJO.Enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
@AllArgsConstructor
@Data
public class TextResponse {
    private long messageId;
   private MessageType type;
    private Date sendDate;
  private  String message;
    private long senderId;
   private long receiverId;


}
