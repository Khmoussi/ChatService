package com.example.messenger.Reposotories;

import com.example.messenger.POJO.Message;
import com.example.messenger.POJO.Response.TextResponse;
import com.example.messenger.POJO.Text;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.messenger.POJO.Response.CoworkerListResponse;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("select new com.example.messenger.POJO.Response.TextResponse(m.id,m.type,m.sendDate,m.message,m.sender.id,m.receiver.id) from Text m where m.receiver.id= :id and m.sender.id = :otherUserId or m.receiver.id= :otherUserId and m.sender.id = :id and m.type<>3")
    List<TextResponse> getMessagesByOtherUserId(long id, long otherUserId);
@Transactional
    @Modifying
    @Query(value = "update message "+
    "set type = 3 "+
    "where sender_id= :currentUserId and id_message= :messageId",nativeQuery = true)
    int deleteMessage(@Param("currentUserId") String currentUserId, @Param("messageId") long messageId);

    @Query("select t from Text t where (t.sender.email = :email and t.receiver.email = :currentUserEmail) or (t.sender.email = :currentUserEmail and t.receiver.email = :email)")
    List<Text> getTexts(@Param("email") String email, @Param("currentUserEmail") String currentUserEmail);

}
