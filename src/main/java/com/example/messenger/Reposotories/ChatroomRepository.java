package com.example.messenger.Reposotories;

import com.example.messenger.POJO.ChatRoom;
import com.example.messenger.POJO.ChatroomMessage;
import com.example.messenger.POJO.Response.ChatroomResponse;
import com.example.messenger.POJO.Response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatroomRepository extends JpaRepository<ChatRoom,Long> {

    @Query("select count(*) from ChatRoom c where admin.id =:id ")
    int checkAdmin(@Param("id") String id);
@Modifying
@Transactional
    @Query("delete from Session s where s.chatRoom.id =:chatRoomId and s.chatRoom.admin.id =:adminId and s.user.id =:removedUserId ")
    int removeUserFromRoom(@Param("chatRoomId") long chatRoomId,@Param("adminId") String adminId,@Param("removedUserId") String removedUserId);
@Query("select c from ChatRoom c")
List<ChatRoom> getMessages();

@Query("select new com.example.messenger.POJO.Response.UserResponse(s.user.email,s.user.firstName,s.user.lastName,s.user.photo) from Session s where s.chatRoom.id =:id  ")
    List<UserResponse> getRoomUser(@Param("id") long id);
}
