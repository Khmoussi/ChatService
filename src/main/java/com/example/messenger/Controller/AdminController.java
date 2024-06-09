package com.example.messenger.Controller;

import com.example.messenger.POJO.User;
import com.example.messenger.Reposotories.UserRepository;
import com.example.messenger.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
public final UserService userService;
    @DeleteMapping("/admin/delete/removeUser")
//only admin has the authority
    ResponseEntity<Boolean> removeUser(@RequestBody String userId, Principal principal) {
        this.userService.deleteUserById(userId);
        return ResponseEntity.ok().body(true);


    }

}
