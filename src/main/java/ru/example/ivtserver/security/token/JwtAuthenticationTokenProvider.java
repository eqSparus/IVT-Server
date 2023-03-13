package ru.example.ivtserver.security.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class JwtAuthenticationTokenProvider extends JwtTokenProvider {

    protected JwtAuthenticationTokenProvider(String secretValue, Long lifeTime) {
        super(secretValue, lifeTime);
    }

    public abstract Optional<String> getToken(HttpServletRequest request);
}
