package com.example.messenger.Services;

import com.example.messenger.POJO.*;
import com.example.messenger.POJO.Enums.MessageType;
import com.example.messenger.POJO.Request.*;
import com.example.messenger.POJO.Response.ChatroomMessageResponse;
import com.example.messenger.POJO.Response.UserResponse;
import com.example.messenger.Reposotories.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService{
    public final ChatroomRepository chatroomRepository;
    public final UserRepository userRepository;
    public final MessageRepository messageRepository;
    public final ChatroomMessageRepository chatroomMessageRepository;
public  final SessionUserRepository sessionRepository;
@Transactional
    public ChatRoom  createChatroom(String chatroomName, User currentUser){
     ChatRoom  chatroom =new ChatRoom(chatroomName,currentUser);
     try {

           this.chatroomRepository.save(chatroom);
         createSession(currentUser, chatroom);

     }catch(IllegalArgumentException e){
         return null;
     }
      return chatroom;
   }
     void createSession(User user,ChatRoom chatRoom){

     Session session=  new Session(user,chatRoom);
       this.sessionRepository.save(session);

   }

   //would be easier to access to the session and fget the current user directly
   public int addUserToChatroom(AddUserToChatroomRequest addUserToChatroomRequest, User currentUser){
       try {

           ChatRoom chatRoom=(ChatRoom) this.chatroomRepository.findById(addUserToChatroomRequest.getChatRoomId()).orElse(null);
        if(chatRoom==null)
            return 3;
           User addedUser = this.userRepository.findById(addUserToChatroomRequest.getAddedUserId()).orElse(null);
           if(addedUser==null)
               return 2;
if(senderExistsInChatroom(chatRoom.getId(),addedUser)){
    return 0;
}
           if((chatRoom !=null )&& (addedUser!=null) ) {
               if (this.chatroomRepository.checkAdmin(currentUser.getEmail()) > 0) {
                   createSession(addedUser, chatRoom);
               } else {
                   return 4;
               }
           }else {
               return -1;
           }

       }catch (IllegalArgumentException e){
           e.printStackTrace();
           return -1;
       }
       return 1;

   }

    public boolean saveMessageInChatroom(ChatroomMessageRequest chatroomMessageRequest, User currentUser) {
        try {

            if (senderExistsInChatroom(chatroomMessageRequest.getRoomId(), currentUser)) {

                ChatRoom chatRoom = this.chatroomRepository.findById(chatroomMessageRequest.getRoomId()).orElse(null);
                if (chatRoom != null) {
                    ChatroomMessage message = new ChatroomMessage(chatroomMessageRequest.getContent(), Timestamp.valueOf(LocalDateTime.now()), MessageType.chat, currentUser, chatRoom,chatroomMessageRequest.getContentType());
                    this.chatroomMessageRepository.save(message);
                    return true;
                }

            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;

        }


    }

    public boolean senderExistsInChatroom(long roomId, User currentUser) {
        if(        this.sessionRepository.checkSession(roomId,currentUser.getEmail())>0)
            return true;
        return false;

    }

    public List<ChatRoom> getUserChatrooms(User currentUser) {
    return this.sessionRepository.findAllByUserId(currentUser.getEmail());
    }

    public List<ChatroomMessageResponse> getRoomMessages(long roomId) {
        return this.chatroomMessageRepository.findAllByRoomId(roomId);
    }

    public String getUsernameById(long receiverId) {
       return  this.userRepository.getUsernameById(receiverId);
    }
    public boolean deleteChatroomMessage(DeleteRoomMessageRequest request, Principal principal){
        //user_id
        String user_id =this.userRepository.findByEmail(principal.getName()).getEmail();
        //verify user is in room //        //verify that the user sent the message
       int i;
        i=this.chatroomMessageRepository.verify(request.getRoomId(),request.getMessageId(),user_id);
        // delete message
        if(i>0){
           ChatroomMessage chatroomMessage= this.chatroomMessageRepository.findById(request.getMessageId()).orElse(null);
           chatroomMessage.setMessageType(MessageType.deleted);
        //   chatroomMessage.setContent("");
           this.chatroomMessageRepository.save(chatroomMessage);
           return true;
        }
        return false;
    }

@Transactional
    public int deleteRoom(DeleteRoomRequest request, Principal principal) {
        String userId=this.userRepository.findByEmail(principal.getName()).getEmail();
        try {
          if(  this.chatroomRepository.checkAdmin(principal.getName())>0) {
              if (this.sessionRepository.deleteSession(request.getRoomId()) > 0) {
                  this.chatroomMessageRepository.deleteChatroomMessagesByRoomId(request.getRoomId());
                  this.chatroomRepository.deleteById(request.getRoomId());
                  return 1;
              }
          }else{
              return 2;
          }
        }catch (IllegalArgumentException e){
            e.printStackTrace();

        }
    return -1;
    }



    public boolean removeUserFromRoom(RemoveUserFromRoom removeUserFromRoom, String adminId) {
        try {
            if (this.chatroomRepository.removeUserFromRoom(removeUserFromRoom.getChatRoomId(), adminId, removeUserFromRoom.getRemovedUserId()) > 0)
                return true;
            return false;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }

    }


    public List<UserResponse> getRoomUser(long id) {
   return this.chatroomRepository.getRoomUser(id);
    }
}


