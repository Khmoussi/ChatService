package com.example.messenger.POJO;

import com.example.messenger.POJO.Enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@AllArgsConstructor
@Builder

public class TestMessage   {
    String content;
    MessageType type;
    String sender;
}
