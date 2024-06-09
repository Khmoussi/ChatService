package com.example.messenger.Controller;
import com.example.messenger.POJO.Request.UserRequest;
import com.example.messenger.POJO.Response.CoworkerListResponse;
import com.example.messenger.POJO.Response.LoginResponse;
import com.example.messenger.POJO.Response.UserResponse;
import com.example.messenger.utilities.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.messenger.POJO.User;
import com.example.messenger.Reposotories.UserRepository;
import com.example.messenger.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/v1/auth" ,produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationContorller {
    private final JwtAuthenticationService jwtAuthenticationService;
    public final SimpMessagingTemplate simpMessagingTemplate;



    public final AuthenticationService authenticationService;
    public final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationContorller.class);

 /*   @PostMapping("/signup")
    String addUser(@RequestBody UserRequest user) {
        String userId = "";
        try {
                 userId = this.authenticationService.signUp(user);
        }catch (Exception e){
            e.printStackTrace();
        }


        return userId;
    }
*/
    @PostMapping("/signUp")
  ResponseEntity<?> signUp(@RequestBody  AccountRequest request)
    {
        RegisterResponse response= this.jwtAuthenticationService.register(request);
        if(response!=null) {
            this.simpMessagingTemplate.convertAndSend("/topic/NewUser" , new CoworkerListResponse(new ArrayList<>(),response.getEmail(), response.getFirstName(), response.getLastname()));

            return ResponseEntity.ok().body(response);
        }        return ResponseEntity.internalServerError().body("could not save user");
    }
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody AuthenticationRequest request)
    {
        try {


            AuthenticationResponse response = this.jwtAuthenticationService.authenticate(request.getEmail(), request.getPassword());
                return ResponseEntity.ok().body(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("user not found");

        }

    }

   /* @PostMapping("/loginn")
    long loginUser(@RequestBody LoginRequest loginRequest,HttpSession httpSession) {
        try {
            if (this.userRepository.existsByEmail(loginRequest.getEmail())) {
                User user=this.userRepository.findByemailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
                httpSession.setAttribute("user",user);
                httpSession.setAttribute("k","kkkk");
                //testing serialization and it works
                User user2 = (User) httpSession.getAttribute("user");
                System.out.println( "get user from session "+ user2.getLastName());        ;
                return user.getId();


            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;

    }
*/
    @GetMapping("/coworkerList")
    List<User> getAllCoworkers(){
        System.out.println("fetching coorkers");
        return this.userRepository.findAll();
    }
  //test
      @GetMapping("admin/hii")
    String hello(HttpSession httpSession){
        //httpSession.setAttribute("khmoussi","khmoussi");
        //httpSession.setMaxInactiveInterval(800);

        return"hello";
    }
    @GetMapping("/goodbye")
    String logoutSuccess(){return "logout success";}

    @GetMapping("/hello")
    String hello(){
        return "Hello from server";
    }
    @GetMapping("/hello2")
    String hello2(){
        System.out.println("mkjmmfkmelzkfm");
        return "bbbbbbbb";
    }
    @PostMapping("/loginn")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response)  {
return ResponseEntity.ok().body("mkj");

    }


}
