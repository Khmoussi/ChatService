package com.example.messenger.POJO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContainerTokens {
    String images;
    String imgmsg;
    String oneToOneUserImage;
    String roomAudios;
    String oneToOneUserAudio;

}
