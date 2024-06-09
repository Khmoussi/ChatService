package com.example.messenger.utilities;

import com.example.messenger.POJO.User;
import com.example.messenger.Reposotories.AuthenticationRepository;
import com.example.messenger.Services.BlobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {
    private final BlobService blobService;
    private final AuthenticationRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    public RegisterResponse register(AccountRequest accountRequest) {

        User user=new User(accountRequest.getFirstName(),accountRequest.getLastName(),accountRequest.getEmail(), passwordEncoder.encode(accountRequest.getPassword()));
      try {
          this.repository.save(user);
          var accessToken=jwtService.generateToken(user);
          var refreshToken=jwtService.generateRefreshToken(user);
          return  RegisterResponse.builder().accessToken(accessToken)
                  .refreshToken(refreshToken)
                  .roleName(user.getRole().name())
                  .email(user.getEmail())
                  .firstName(user.getFirstName())
                  .Lastname(user.getLastName())
                  .build();
      }catch (Exception e){
          e.printStackTrace();
          return null;
      }

    }
    public  AuthenticationResponse authenticate(String email,String password) throws Exception {



            //will verify if there is a user in the database with these credentials
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password)) ;




//if the authentication manager went with no problems it's time to generate The token
        var userDetails= repository.findById(email) .orElseThrow(() -> new Exception("User not found with email: " + email));
        var accessToken=jwtService.generateToken(userDetails);
        var refreshToken=jwtService.generateRefreshToken(userDetails);
        return  AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .roleName(userDetails.getRole().name())
                .lastname(userDetails.getFirstName())
                .firstname(userDetails.getLastName())
                .email(userDetails.getUsername())
                .photoUrl(userDetails.getPhoto() != null && !userDetails.getPhoto().isEmpty() ? userDetails.getPhoto() : "https://chatdemo.blob.core.windows.net/images/unknown.jpg")
                .containerTokens(this.blobService.getContainerTokens())
                .build();   }

    //getting access token from refresh token: refreshTokenFilter
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken ;
        final String userEmail;

        if(authHeader==null||!authHeader.startsWith("Bearer")){
            return;
        }
        refreshToken=authHeader.substring(7);
        userEmail=jwtService.extractUsername(refreshToken);//to do extract the email from token by creating service for that
        if(userEmail!=null){//even if he is authenticated we can ask for new accessToken
            var userDetails=repository.findById(userEmail).orElseThrow();//it has to throw otherwise if null it won't be a UserDetails Obejet then can't authenticate

            if(jwtService.isTokenValid(refreshToken,userDetails)){


                var accessToken = jwtService.generateToken(userDetails);

                var refreshTokenResponse = RefreshTokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), refreshTokenResponse);
            }
        }

    }
}
