package com.example.messenger.Services;

import com.example.messenger.POJO.User;
import com.example.messenger.POJO.Response.UserResponse;
import com.example.messenger.Reposotories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
public final UserRepository userRepository;
   public List<UserResponse> getUsersList(){
        return this.userRepository.findAllUsers();



    }


    public User findUserByEmail(String email){return this.userRepository.findByEmail(email);}

    public boolean deleteUserById(String userId) {
       try{
           this.userRepository.deleteById(userId);
           return true;
       }
       catch (NullPointerException e){
           e.printStackTrace();
           return false;
       }
    }
}
