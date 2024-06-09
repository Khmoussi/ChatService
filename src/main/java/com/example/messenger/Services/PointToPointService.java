package com.example.messenger.Services;

import com.example.messenger.POJO.*;
import com.example.messenger.POJO.Enums.MessageType;
import com.example.messenger.POJO.Response.CoworkerListResponse;
import com.example.messenger.POJO.Response.TextResponse;
import com.example.messenger.POJO.Response.UserResponse;
import com.example.messenger.Reposotories.MessageRepository;
import com.example.messenger.Reposotories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointToPointService {

    public final MessageRepository messageRepository;
    public final UserRepository userRepository;


    public void saveMessage(Text text){

        this.messageRepository.save(text);
    }

    public List<TextResponse> getMessagesByOtherUserId(long otherUserId, Principal principal) {
       long id= this.userRepository.getIdByUsername(principal.getName());
     return   this.messageRepository.getMessagesByOtherUserId(id,otherUserId);
    }


    public List<CoworkerListResponse> getUsersList(String currentUserEmail){
        List<UserResponse> list =this.userRepository.findAllUsers();
        ArrayList<CoworkerListResponse> coworkerListResponses=new ArrayList<>();
        list.forEach(userResponse -> {
           List<Text> t= this.messageRepository.getTexts(userResponse.getEmail(),currentUserEmail);
            coworkerListResponses.add(new CoworkerListResponse(t,userResponse.getEmail(),userResponse.getFirstName(),userResponse.getLastName(),userResponse.getPhotoName()));
        });
return coworkerListResponses;


    }
    public boolean deleteMessage( long messageId,String userId){
        if(this.messageRepository.deleteMessage(userId,messageId)>0)
            return true;
        return false;
    }



}
