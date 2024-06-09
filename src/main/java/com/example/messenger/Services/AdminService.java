package com.example.messenger.Services;

import com.example.messenger.POJO.User;
import com.example.messenger.Reposotories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AdminService {
    public final UserRepository userRepository;

    boolean removeUser(long adminId,String userId){
      //verif it has the admin authority
        try {

            this.userRepository.deleteById(userId);
            return true;
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }
        }
}
