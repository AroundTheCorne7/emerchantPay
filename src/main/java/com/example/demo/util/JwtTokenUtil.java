package com.example.demo.util;

import com.example.demo.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Constants.EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, Constants.SECRET_KEY)
                .compact();
    }

    public User parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Constants.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        User user = new User();
        user.setUsername(claims.getSubject());
        // You can set other user information from the claims if needed
        return user;
    }
}
