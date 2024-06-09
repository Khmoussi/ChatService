package com.example.messenger.Services;

import com.example.messenger.POJO.Request.UserRequest;
import com.example.messenger.POJO.User;
import com.example.messenger.Reposotories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public final UserRepository userRepository;
    public boolean verifyUserBeforeAdding(User user){

        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        String phoneRegex = "^\\d{8,}$";

        if (user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null || user.getPassword() == null ) {
            return false;
        }



        Pattern emailPattern = Pattern.compile(emailRegex);
        Pattern phonePattern = Pattern.compile(phoneRegex);

        Matcher emailMatcher = emailPattern.matcher(user.getEmail());
        Matcher phoneMatcher = phonePattern.matcher(user.getEmail());

        if (!emailMatcher.matches() && !phoneMatcher.matches()) {
     return false;
        }
        return true;
    }
    public void registerUser(User user){
        this.userRepository.save(user);
    }
    public String signUp(UserRequest request){
        try {
            User user=new User(request.getFirstName(),request.getLastName(),request.getEmail(),request.getPassword());


            return  this.userRepository.save(user).getEmail();

        }catch (IllegalArgumentException e){
            return  "";
        }

    }


}
