package com.example.messenger.POJO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@IdClass(SessionId.class)
public class Session {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Id
    User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @Id
    ChatRoom chatRoom;

    public Session(User user, ChatRoom chatRoom) {
        this.user=user;
        this.chatRoom=chatRoom;
    }
    public Session() {

    }
}
