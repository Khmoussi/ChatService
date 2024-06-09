package com.example.messenger.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service //managed bean
public class JwtService {
    private long accessTokenExpiration=864000000;
    private long refreshTokenExpiration=18640000;
    private static final String secretKey="9FBB6F7992A1F8FBB76981A45564C9FBB6F7992A1F8FBB76981A45564C";

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return  claimsResolver.apply(claims);
    }
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }
    //generating access token
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){


        String token =buildToken(extraClaims,userDetails,accessTokenExpiration);
        return token;

    }
    String buildToken(Map<String,Object> extraClaims,UserDetails userDetails,long expiration){
        String token= Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }
    public String generateToken( UserDetails userDetails){
        return generateToken(new HashMap(),userDetails);
    }

    //generating refresh token
    public String generateRefreshToken(Map<String,Object> extraClaims, UserDetails userDetails){


        String token =buildToken(extraClaims,userDetails,refreshTokenExpiration);
        return token;

    }
    public String generateRefreshToken( UserDetails userDetails){
        return generateRefreshToken(new HashMap(),userDetails);
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
    return extractClaim(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        System.out.println("parsed token "+token);
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
