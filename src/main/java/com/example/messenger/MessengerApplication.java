package com.example.messenger;

import com.example.messenger.POJO.User;
import com.example.messenger.Services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

@SpringBootApplication
@EnableScheduling
@EnableRetry
public class MessengerApplication {
	@Autowired
	private FindByIndexNameSessionRepository<? extends Session> sessionRepository;
	public static void main(String[] args) {
		System.out.println(Timestamp.valueOf(LocalDateTime.now()));
		SpringApplication.run(MessengerApplication.class, args);

	}
/*	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			PasswordEncoder passwordEncoder
	) {





	/*	return args -> {
			var admin = User.builder()
					.firstName("Admin")
					.lastName("Admin")
					.email("admin@gmail.com")
					.password(passwordEncoder.encode("pass123"))
					.role(ADMIN)
					.build();
service.registerUser(admin);

		};
	}*/


}
