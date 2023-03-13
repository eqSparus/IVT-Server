package ru.example.ivtserver.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.Optional;

public interface TokenProvider {


    String generateToken(String subject);


    String generateToken(String subject, Long time);


    String generateToken(Claims claims);


    String generateToken(Claims claims, Long time);

    boolean isValidToken(String token) throws JwtException;

    Optional<Claims> getBody(String token) throws JwtException;
}
