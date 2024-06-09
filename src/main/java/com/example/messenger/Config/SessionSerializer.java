package com.example.messenger.Config;

import com.example.messenger.POJO.Session;
import com.example.messenger.POJO.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SessionSerializer extends JsonSerializer<Session> {
    @Override
    public void serialize(Session session, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {


            jsonGenerator.writeString(session.getUser().getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
