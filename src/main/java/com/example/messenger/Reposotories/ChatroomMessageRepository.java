package com.example.messenger.Reposotories;

import com.example.messenger.POJO.ChatroomMessage;
import com.example.messenger.POJO.Response.ChatroomMessageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatroomMessageRepository extends JpaRepository<ChatroomMessage,Long> {
    @Query(value = "select new com.example.messenger.POJO.Response.ChatroomMessageResponse(u.idMessage,u.Content,u.sendDate,u.messageType,u.user.id,u.chatRoom.id)  from ChatroomMessage u where u.chatRoom.id = :roomId and u.messageType<>3")
    List<ChatroomMessageResponse> findAllByRoomId(@Param("roomId") long roomId);
@Query(value = "select count(u.user_id) from user u ,session s where u.user_id = :userId and u.user_id = s.user_id and s.room_id= :roomId and " +
        "u.user_id in (select sender_id from chatroom_message where id_message= :messageId);",nativeQuery = true)
    int verify(long roomId, long messageId,String userId);
@Modifying
@Query(value = "delete from chatroom_message where room_id = :roomId",nativeQuery = true)
int deleteChatroomMessagesByRoomId(@Param("roomId") long roomId);


}
