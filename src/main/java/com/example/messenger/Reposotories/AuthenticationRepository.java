package com.example.messenger.Reposotories;

import com.example.messenger.POJO.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<User,String> {
}
