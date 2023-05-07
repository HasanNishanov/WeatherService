package com.company.utill;

import com.company.model.dto.UserDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtUtil {
    private static final String secretKey = "";

    public String generateToken(UserDTO user) {
        Date now = new Date();
        Date expiryDate = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000));

        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());
        claims.put("subscriptions", user.getSubscriptions());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public static Integer decode(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        Integer id = (Integer) claims.get("id");
        return id;
    }
}