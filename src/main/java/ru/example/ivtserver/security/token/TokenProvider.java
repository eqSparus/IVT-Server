package ru.example.ivtserver.security.token;

import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;

public interface TokenCollector {

    @NonNull
    String generateToken(@NonNull String email);

    @NonNull
    String generateToken(@NonNull String email, @NonNull Long time);

    boolean isValidToken(@NonNull String token);

    @NonNull
    Claims getBody(@NonNull String token);

}
