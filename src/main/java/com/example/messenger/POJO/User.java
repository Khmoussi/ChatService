package com.example.messenger.POJO;

import com.example.messenger.utilities.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable, UserDetails {

    @Column(name = "user_id")

     @Id
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String photo;
    Role role = Role.User;

    @Transient //used to ignore these fields when serializing (to save in ession attributes)
    @OneToMany(mappedBy = "sender") List<Message> messages;
    @Transient

    @OneToMany(mappedBy = "user")
    private List<Session> sessions;
    @Transient

    @OneToMany(mappedBy = "admin")
    private List<ChatRoom> createdChatRooms;
    @Transient

    @OneToMany(mappedBy = "user")
    private List<ChatroomMessage> chatroomMessages;

    public User(String firstName, String lastName, String email, String password) {
        this.email=email;
        this.password=password;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getUsername() {
        return this.email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public String getPassword() {
        return password;
    }

}
