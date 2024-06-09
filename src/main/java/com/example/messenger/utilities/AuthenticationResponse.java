package com.example.messenger.utilities;

import com.example.messenger.POJO.Text;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class AuthenticationResponse {
    private String firstname;
    private String lastname;
    private String email;
    private String roleName;
    String photoUrl="";

    private String accessToken;
    private String refreshToken;
    private HashMap<String,String> containerTokens;
}
