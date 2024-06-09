package com.example.messenger.POJO;

import com.example.messenger.Config.MessageUserSerializer;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {

    public ChatRoom(String name,User admin){
        this.roomName=name;
        this.admin=admin;
    }

    @Column(name = "room_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long id;

    private  String roomName;
    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom")
    private List<Session> sessions;
    @JsonSerialize(using = MessageUserSerializer.class)

    @ManyToOne

    @JoinColumn(name = "admin_id")
    private User admin;
    @OneToMany(mappedBy = "chatRoom")
    private List<ChatroomMessage> chatroomMessages;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<ChatroomMessage> getChatroomMessages() {
        return chatroomMessages;
    }

    public void setChatroomMessages(List<ChatroomMessage> chatroomMessages) {
        this.chatroomMessages = chatroomMessages;
    }
}
