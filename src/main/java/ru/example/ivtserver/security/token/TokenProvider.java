package ru.example.ivtserver.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface TokenProvider {

    @NonNull
    String generateToken(@NonNull String subject);

    @NonNull
    String generateToken(@NonNull String subject, @NonNull Long time);

    @NonNull
    String generateToken(@NonNull Claims claims);

    @NonNull
    String generateToken(@NonNull Claims claims, @NonNull Long time);

    boolean isValidToken(@NonNull String token) throws JwtException;

    Optional<Claims> getBody(@NonNull String token) throws JwtException;
}
