package ru.example.ivtserver.security.token;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface TokenProvider {

    @NonNull
    String generateToken(@NonNull String email);

    @NonNull
    String generateToken(@NonNull String email, @NonNull Long time);

    boolean isValidToken(@NonNull String token);

    @NonNull
    Claims getBody(@NonNull String token);

    @NonNull
    Optional<String> getToken(@NonNull HttpServletRequest request);

    @NonNull
    String getEmailFromToken(@NonNull String token);

}
