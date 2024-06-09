package com.example.messenger.Reposotories;

import com.example.messenger.POJO.User;
import com.example.messenger.POJO.Response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
   User findByEmail(String email);

   boolean existsByEmail(String email);
   @Query("select u from User u where u.password = ?2 and u.email = ?1")
   User findByemailAndPassword(String email,String password);

@Query("select new com.example.messenger.POJO.Response.UserResponse(c.email,c.firstName,c.lastName,c.photo) from User c ")
   List<UserResponse> findAllUsers();
@Query("select u.email from User u where u.id= :receiverId ")
    String getUsernameById(@Param("receiverId") long receiverId);
@Query("select u.id from User u where u.email= :name")
    long getIdByUsername(@Param("name") String name);

@Modifying
@Transactional
@Query("update User u set u.photo =:url where u.email =:email ")
    int updateImageUrl(@Param("url") String s, @Param("email") String email);
}
