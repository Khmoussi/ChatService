package com.example.messenger.Reposotories;

import com.example.messenger.POJO.ChatRoom;
import com.example.messenger.POJO.Response.ChatroomResponse;
import com.example.messenger.POJO.Session;
import com.example.messenger.POJO.SessionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SessionUserRepository extends JpaRepository<Session, SessionId> {
@Query("select count(*) from Session s where s.chatRoom.id =:roomId and s.user.id =:id")
    int checkSession(@Param("roomId") long roomId, @Param("id") String id);
@Query("select s.chatRoom from Session s where s.user.id =:id")
    List<ChatRoom> findAllByUserId(@Param("id") String id);
@Modifying
@Transactional
@Query("delete from Session s  where s.chatRoom.id =:roomId ")
    int deleteSession(@Param("roomId") long roomId);
}
