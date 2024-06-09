package com.example.messenger.POJO.Response;

import com.example.messenger.POJO.Text;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CoworkerListResponse {
    String firstName;
    String lastName;
    String email;
    List<Text> messages;
    String photoUrl="";


    public CoworkerListResponse(List<Text> message, String email, String firstName, String lastName,String photoUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.messages = message;
        this.photoUrl=photoUrl;

    }
    public CoworkerListResponse(List<Text> message, String email, String firstName, String lastName ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.messages = message;

    }
}
