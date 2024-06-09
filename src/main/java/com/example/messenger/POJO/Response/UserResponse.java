package com.example.messenger.POJO.Response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserResponse  {
    private String email;
    private String firstName;
    private String lastName;
    private String photoName="";

    public UserResponse(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public UserResponse(String email, String firstName, String lastName,String photoName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoName=photoName;
        if(photoName ==null)
            this.photoName="https://chatdemo.blob.core.windows.net/images/unknown.jpg";
    }
}
