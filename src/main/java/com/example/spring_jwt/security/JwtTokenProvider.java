package com.example.spring_jwt.security;

import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.util.StringUtils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.spring_jwt.model.UserEntity;



@Component
public class JwtTokenProvider {

    Logger log= LoggerFactory.getLogger(this.getClass());

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;

    @Value("${app.security.jwt.expiration}")
    private Long jwtDurationSeconds;


    public String generateToken(Authentication authentication) {
        UserEntity user= (UserEntity) authentication.getPrincipal();

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()),SignatureAlgorithm.HS256)
                .setHeaderParam("typ", "JWT")
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (jwtDurationSeconds * 1000)))
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .compact();
    
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
                    
            return claims.get("username", String.class); // Extrae el "username" del token
        } catch (Exception e) {
            log.info("Error al obtener el username del token", e);
            return null;
        }
    }
    

    public boolean isValidToken(String token) {
        if (!StringUtils.hasLength(token)) {
            return false;
        }
        try {
            JwtParser validator = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build();
            validator.parseClaimsJws(token);
            return true;    
        } catch (SignatureException e) {
            log.info("Error en la firma del token", e);
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.info("Token incorrecto", e);
        } catch (ExpiredJwtException e) {
            log.info("Token expirado", e);
        }
        return false;
    }
    
}
