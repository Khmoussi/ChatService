package com.example.messenger.Controller;

import com.example.messenger.POJO.*;
import com.example.messenger.POJO.Enums.MessageType;
import com.example.messenger.POJO.Request.*;
import com.example.messenger.POJO.Response.*;
import com.example.messenger.Reposotories.ChatroomRepository;
import com.example.messenger.Services.BlobService;
import com.example.messenger.Services.ChatRoomService;
import com.example.messenger.Services.PointToPointService;
import com.example.messenger.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/chat" ,produces = MediaType.APPLICATION_JSON_VALUE)
 public  class ChatController {
    public final UserService userService;
    public final ChatRoomService chatRoomService;
    public final PointToPointService pointToPointService;
    public final SimpMessagingTemplate simpMessagingTemplate;
    public final RedisTemplate<String, Object> redisTemplate;
    public final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    public final ChatroomRepository chatroomRepository;
    public final BlobService blobService;


    @MessageMapping("/public")
    public void sendMesssage(@Payload String message) {

        System.out.println("Message received: " + message);
        try {
            // Send the message to the "/topic/public" destination
            this.simpMessagingTemplate.convertAndSend("/topic/public", message);
            System.out.println("Message sent successfully: " + message);
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    /*@MessageMapping("/addUserToAllChat")
    @SendTo("/topic/public")
    public TestMessage addUser(@Payload TestMessage message,SimpMessageHeaderAccessor simpMessageHeaderAccessor){
        simpMessageHeaderAccessor.getSessionAttributes().put("username",message.getSender());
        return message;

    }
   */


    @MessageMapping("/{roomId}")
    public void subscribeToRoomByIdd(String message, @DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor,Principal principal) {
        String contentType;
        try {


            contentType = headerAccessor.getNativeHeader("messageType").get(0);
        }catch (Exception e){
            e.printStackTrace();
            contentType="text";
        }
        User user = (User) ((Authentication) principal).getPrincipal();
        if(this.chatRoomService.senderExistsInChatroom(Long.parseLong(roomId),user))
            saveMessage(roomId,user,message,MessageType.chat,contentType);
    }
    //save and send room msg
    public void saveMessage(String roomId,User user ,String message,MessageType messageType ,String contentType){

            System.out.println("message received " + message);
            LocalDate currentDate = LocalDate.now();
            this.simpMessagingTemplate.convertAndSend("/topic/" + roomId, new ChatroomMessageResponse(message, Date.valueOf(currentDate), MessageType.chat, user.getEmail(), Long.parseLong(roomId),contentType));
            // String userEmail = headerAccessor.getSessionAttributes().get("userEmail").toString();
            //    User currentUser = this.userService.findUserByEmail(userEmail);

            this.chatRoomService.saveMessageInChatroom(new ChatroomMessageRequest(message, Long.parseLong(roomId), messageType,contentType), user);

    }

    @MessageMapping("/NewUser")
    public void subscribeToNewUser(){

    }
    //point to point Messaging

    @MessageMapping("/point")
    void sendtopoint(@Payload  Message<String> message){

        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);
        String receiverId = accessor.getNativeHeader("receiverId").get(0);
        System.out.println("header : " + receiverId);
        simpMessagingTemplate.convertAndSendToUser(receiverId, "/queue/hello", message.getPayload());

    }
    @MessageMapping("/chat")
    void specificUser(Message<String> message, Principal principal) {
        System.out.println(message + principal.getName());
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);
System.out.println("chat orkingvfel,");
        try {
            String receiverId = accessor.getNativeHeader("receiverId").get(0);
            System.out.println("header : " + receiverId);
            User user = (User) ((Authentication) principal).getPrincipal();

            if (this.userService.userRepository.existsByEmail(receiverId)) {
               saveUserMessage(user,receiverId,message.getPayload(),"text");
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            System.out.println(" receiver Id not found in header");

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println(" Receiver user not found");
        }
        // how to retreive anything you want
       /*  MessageHeaders headers = message.getHeaders();

        Map<String, List<String>> nativeHeaders = headers.get("nativeHeaders", Map.class);

       // Now you can iterate through nativeHeaders to retrieve specific headers
        for (String headerName : nativeHeaders.keySet()) {
            List<String> values = nativeHeaders.get(headerName);
            for (String value : values) {
                System.out.println(headerName + ": " + value);
            }
        }

       */

    }
    public void saveUserMessage(User user ,String receiverId ,String message,String contentType){
        User receiver= this.userService.userRepository.findById(receiverId).orElseThrow();
        System.out.println("receiver name : " + receiverId);
        Text text=new Text(user,receiver,MessageType.chat, Timestamp.valueOf(LocalDateTime.now()),message,contentType);
        simpMessagingTemplate.convertAndSendToUser(receiverId, "/queue/hello", text);
        this.pointToPointService.saveMessage(text);
    }

@MessageMapping("/usersListUpdates")
void updateUsersList(){

}

    @MessageMapping("/addUserToRoom")
    void addUserToRoom(){


    }
    @MessageMapping("/deleteUserFromRoom")
    void deleteUserFromRoom(){


    }


    @PostMapping("/createChatroom")
    ResponseEntity<?> createChatroom(@RequestBody CreateRoomRequest request, Principal principal,HttpSession httpSession) {
        ChatRoom returnValue = null;
        System.out.println("room name: " + request.getName());

        try {
            if (request.getName() != null) {
                /*
// Get the SecurityContext from HttpSession
no longer need since we swapped to jwt wich is stateless
                Object securityContext = httpSession.getAttribute("SPRING_SECURITY_CONTEXT");

                if (securityContext != null && securityContext instanceof SecurityContext) {
                    // Get the Authentication object from SecurityContext
                    Authentication authentication = ((SecurityContext) securityContext).getAuthentication();

                    if (authentication != null && authentication.isAuthenticated()) {
                        // Get the username from Authentication object
                        User currentUser = (User) authentication.getPrincipal();
                        returnValue = this.chatRoomService.createChatroom(request.getName(), currentUser);

                    }

                } else {
                    return ResponseEntity.badRequest().body(" user not authorized ");

                }

                 */
                //

                User user = (User) ((Authentication) principal).getPrincipal();
                    returnValue = this.chatRoomService.createChatroom(request.getName(), user);
returnValue.setChatroomMessages(new ArrayList<>());

             if(returnValue!=null)
                return ResponseEntity.ok(new ChatroomResponseWithUsers(returnValue,new ArrayList<>()));
            }
            return ResponseEntity.badRequest().body("room must have a name");

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("unknown error");

        }

    }

    @PostMapping("/addUserToChatroom")
    ResponseEntity<?> addUserToChatroom(@RequestBody AddUserToChatroomRequest addUserToChatroomRequest, Principal principal,HttpSession httpSession) {
    /*
        // Get the SecurityContext from HttpSession
        User currentUser = null;
        Object securityContext = httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null && securityContext instanceof SecurityContext) {
            // Get the Authentication object from SecurityContext
            Authentication authentication = ((SecurityContext) securityContext).getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                // Get the username from Authentication object
                currentUser = (User) authentication.getPrincipal();
            }
        }else{
            return ResponseEntity.badRequest().body(" user not authorized ");
        }

     */
        LocalDate currentDate = LocalDate.now();

        try {
            User user = (User) ((Authentication) principal).getPrincipal();
            User addedUser;
            //
            int result = this.chatRoomService.addUserToChatroom(addUserToChatroomRequest, user);
            try {
                 addedUser = this.userService.findUserByEmail(addUserToChatroomRequest.getAddedUserId());
            }catch (Exception e){
                e.printStackTrace();

                return ResponseEntity.badRequest().body("user  not found");

            }
            if (result == 1) {
                ChatRoom room = this.chatroomRepository.findById(addUserToChatroomRequest.getChatRoomId()).orElseThrow();
               ChatroomResponseWithUsers response=new ChatroomResponseWithUsers(room,this.chatRoomService.getRoomUser(room.getId()));
               this.simpMessagingTemplate.convertAndSendToUser(addUserToChatroomRequest.getAddedUserId(), "/queue/addUserToRoom", response);

                this.simpMessagingTemplate.convertAndSend("/topic/" + addUserToChatroomRequest.getChatRoomId(), new ChatroomMessageResponse(addedUser.getFirstName() +" "+addedUser.getLastName()+" joined the room", Date.valueOf(currentDate), MessageType.join,user.getEmail(),addUserToChatroomRequest.getChatRoomId(),new UserResponse(  addedUser.getEmail(), addedUser.getFirstName(), addedUser.getLastName())));


                // return ResponseEntity.ok().body(room);
                System.out.println("user added successfully");
                return ResponseEntity.ok().body("user added successfully");
            }
            if (result == 0)
                return ResponseEntity.badRequest().body("user already exists in this room");
            if(result==2)
                return ResponseEntity.badRequest().body("user  not found");
            if(result==3)
                return ResponseEntity.badRequest().body("room  not found");
            if(result==4)
                return ResponseEntity.badRequest().body("you are not an admin in this chatroom");

            return ResponseEntity.badRequest().body("user cannot be added to this room");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("unknown error");

        }

    }


    // get chatCoworkers
    @GetMapping("/getCoworkersList")//deprecated
    ResponseEntity<List<UserResponse>> getUsersList( ) {
        List<UserResponse> list;
        list = this.userService.getUsersList();
        if (list != null) {
            if (list.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            System.out.println("coworker list returned");
            return ResponseEntity.ok().body(list);
        }
        return (ResponseEntity<List<UserResponse>>) ResponseEntity.EMPTY;
    }

    @GetMapping("/getUserChatrooms")
    ResponseEntity<?> getUserChatrooms(HttpSession httpSession, Principal principal) {
        // Get the SecurityContext from HttpSession
      /*  User currentUser = null;
        Object securityContext = httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext != null && securityContext instanceof SecurityContext) {
            // Get the Authentication object from SecurityContext
            Authentication authentication = ((SecurityContext) securityContext).getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                // Get the username from Authentication object
                currentUser = (User) authentication.getPrincipal();

            }
        }*/
        //
        User user = (User) ((Authentication) principal).getPrincipal();
        List<ChatroomResponseWithUsers> result =new ArrayList<>();
        try {
            List<ChatRoom> list = this.chatRoomService.getUserChatrooms(user);
            list.forEach(chatRoom -> {
                List<UserResponse> l=this.chatRoomService.getRoomUser(chatRoom.getId());
                result.add(new ChatroomResponseWithUsers(chatRoom,l));
            });
            return ResponseEntity.ok().body(result);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("unknown error");

        }
    }

    @GetMapping("/userMessages")
    ResponseEntity<List<ChatroomMessageResponse>> getMessagesByRoomId(@RequestBody long roomId) {
        List<ChatroomMessageResponse> list = this.chatRoomService.getRoomMessages(roomId);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/getCoworkerMessages")
    ResponseEntity<List<TextResponse>> getCoworkerMessages(@RequestBody String otherUserId, Principal principal) {

        try {
            List<TextResponse> list = this.pointToPointService.getMessagesByOtherUserId(Long.parseLong(otherUserId), principal);
            return ResponseEntity.ok(list);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return (ResponseEntity<List<TextResponse>>) ResponseEntity.EMPTY;

    }

    @DeleteMapping("/delete/roomMessage")
    ResponseEntity<Boolean> deleteChatroomMessage(@RequestBody DeleteRoomMessageRequest request, Principal principal) {

        boolean i = this.chatRoomService.deleteChatroomMessage(request, principal);
        return ResponseEntity.ok().body(i);
    }

    //delete room
    @DeleteMapping("/delete/room")
    ResponseEntity<?> deleteRoom(@RequestBody DeleteRoomRequest request, Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();

        int result=this.chatRoomService.deleteRoom(request, principal);

        if ( result== 2)
            return ResponseEntity.badRequest().body("you are not the admin");
        if ( result== 1) {
            LocalDate currentDate = LocalDate.now();
            this.simpMessagingTemplate.convertAndSend("/topic/" + request.getRoomId(), new ChatroomMessageResponse("room deleted", Date.valueOf(currentDate), MessageType.deleted, user.getEmail(), request.getRoomId()));
            return ResponseEntity.ok().body("Room deleted successfully");
        }
        return ResponseEntity.internalServerError().body("unknown error");


    }

    //admin remove user from  room
    @DeleteMapping("/delete/removeUserFromRoom")
    ResponseEntity<?> removeUserFromRoom(@RequestBody RemoveUserFromRoom removeUserFromRoom, Principal principal) {
        LocalDate currentDate = LocalDate.now();
        User removedUser;
        User user = (User) ((Authentication) principal).getPrincipal();

        try {
            removedUser = this.userService.findUserByEmail(removeUserFromRoom.getRemovedUserId());
        }catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.badRequest().body("user you want to remove doesn't exists");

        }
        try {
            boolean result = this.chatRoomService.removeUserFromRoom(removeUserFromRoom, user.getEmail());
            if (result) {
                this.simpMessagingTemplate.convertAndSendToUser(removeUserFromRoom.getRemovedUserId(), "/queue/removedUserFromRoom", removeUserFromRoom.getChatRoomId());

                this.simpMessagingTemplate.convertAndSend("/topic/" + removeUserFromRoom.getChatRoomId(), new ChatroomMessageResponse(removedUser.getFirstName() + " " + removedUser.getLastName() + " removed from the room", Date.valueOf(currentDate), MessageType.Leave, user.getEmail(), removeUserFromRoom.getChatRoomId(), new UserResponse(removedUser.getEmail(), removedUser.getFirstName(), removedUser.getLastName())));

                return ResponseEntity.ok().body("User removed successfully");
            }
        }catch (Exception e){
            e.printStackTrace();
        return ResponseEntity.internalServerError().body("Unknown error");
    }
        return ResponseEntity.internalServerError().body("Unknown error");

    }

    //delete one to one message
    @DeleteMapping("/delete/removeOneToOneUserMessage")
    ResponseEntity<Boolean> removeOneToOneUserMessage(@RequestBody long messageId, Principal principal) {
        User currentUser = (User) ((Authentication) principal).getPrincipal();
        String currentUserId = currentUser.getEmail();
        boolean result = this.pointToPointService.deleteMessage(messageId, currentUserId);
        return ResponseEntity.ok().body(result);

    }
@GetMapping("/getUsers")
ResponseEntity<?>getUsers(Principal principal){
        try {
            User user = (User) ((Authentication) principal).getPrincipal();
           List <CoworkerListResponse> result=  this.pointToPointService.getUsersList(user.getEmail());
       return ResponseEntity.ok().body( result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("unknown error");
        }
}


    @GetMapping("/getMsg")
    ResponseEntity<?> getMessages(){
        try {
            return ResponseEntity.ok().body(this.chatroomRepository.getMessages());
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(this.chatroomRepository.getMessages());

    }
   /* @GetMapping("admin/hi")
    String hello(){



        return"hello";
    }*/ // iget interneal server error idk why
   /* @PostMapping("/TestUser")
    String testUser(@RequestBody UserRequest request){
        User user=new User(request.getFirstName(),request.getLastName(),request.getEmail(),request.getPassword());

        return " ";
    }
*/
    @PostMapping("/upload/room/imgMsg")
    ResponseEntity<?> uploadRoomMsg(@RequestParam("file") MultipartFile file , @RequestParam("request") String requestJson,Principal principal){
       try {
           RoomIdRequest request =new ObjectMapper().readValue(requestJson,RoomIdRequest.class);
          User user= (User)((Authentication )principal).getPrincipal();
           //save and send room msg
           if(this.chatRoomService.senderExistsInChatroom(Long.parseLong(request.getRoomId()),user)) {
               String url=  this.blobService.uploadImgMessage(file, Long.parseLong(request.getRoomId()));
               saveMessage(request.getRoomId(), user, url, MessageType.chat,"image");
                   return ResponseEntity.ok().body(url);
           }
           return  ResponseEntity.badRequest().body("user doesn't exist in this room  ");
       }catch (Exception e){
           e.printStackTrace();
           return ResponseEntity.internalServerError().body("unknown error");
       }
    }

    @PostMapping("/upload/user/imgMsg")
    ResponseEntity<?> uploadUserMsg(@RequestParam("file") MultipartFile file , @RequestParam("request") String requestJson,Principal principal){
        try {
            ReceiverIdRequest request =new ObjectMapper().readValue(requestJson,ReceiverIdRequest.class);
            User user= (User)((Authentication )principal).getPrincipal();
            //save and send room msg
            if (this.userService.userRepository.existsByEmail(request.getReceiverId())) {
                String url=  this.blobService.uploadUserMessage(file, request.getReceiverId());
                saveUserMessage( user , request.getReceiverId() , url, "image");
                return ResponseEntity.ok().body(url);
            }
            return  ResponseEntity.badRequest().body("user doesn't exist in this room  ");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("unknown error");
        }
    }

    @PostMapping("/upload/room/audioMsg")
    ResponseEntity<?> uploadRoomAudioMsg(@RequestParam("file") MultipartFile file , @RequestParam("request") String requestJson,Principal principal){
        try {
            RoomIdRequest request =new ObjectMapper().readValue(requestJson,RoomIdRequest.class);
            User user= (User)((Authentication )principal).getPrincipal();
            //save and send room msg
            if(this.chatRoomService.senderExistsInChatroom(Long.parseLong(request.getRoomId()),user)) {
                String url=  this.blobService.uploadRoomAudioMessage(file, Long.parseLong(request.getRoomId()));
                saveMessage(request.getRoomId(), user, url, MessageType.chat,"audio");
                return ResponseEntity.ok().body(url);
            }
            return  ResponseEntity.badRequest().body("user doesn't exist in this room  ");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("unknown error");
        }
    }

    @PostMapping("/upload/user/audioMsg")
    ResponseEntity<?> uploadUserAudioMsg(@RequestParam("file") MultipartFile file , @RequestParam("request") String requestJson,Principal principal){
        try {
            ReceiverIdRequest request =new ObjectMapper().readValue(requestJson,ReceiverIdRequest.class);
            User user= (User)((Authentication )principal).getPrincipal();
            //save and send room msg
            if (this.userService.userRepository.existsByEmail(request.getReceiverId())) {
                String url=  this.blobService.uploadUserAudioMessage(file, request.getReceiverId());
                saveUserMessage( user , request.getReceiverId() , url, "audio");
                return ResponseEntity.ok().body(url);
            }
            return  ResponseEntity.badRequest().body("user doesn't exist in this room  ");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("unknown error");
        }
    }

}