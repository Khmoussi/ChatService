package com.example.messenger.utilities;

import lombok.Data;

@Data

public class AuthenticationRequest {
    private String email;
    private String password;
}
