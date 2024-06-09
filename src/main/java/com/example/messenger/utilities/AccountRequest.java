package com.example.messenger.utilities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AccountRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    public AccountRequest(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
