package com.example.messenger.Config;


import com.example.messenger.POJO.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class MessageUserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {


            jsonGenerator.writeString(user.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}