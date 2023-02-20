package ru.example.ivtserver.security.token;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;

public interface TokenCollector {

    @NonNull
    String generateToken(@NonNull String email);

    @NonNull
    String generateToken(@NonNull String email, @NonNull Long time);

    @NonNull
    String getToken(@NonNull HttpServletRequest request);

    @NonNull
    String getToken(@NonNull String token);

    boolean isValidToken(@NonNull String token);

    @NonNull
    String getEmailFromToken(@NonNull String token);

    @NonNull
    Claims getBody(@NonNull String token);

}
