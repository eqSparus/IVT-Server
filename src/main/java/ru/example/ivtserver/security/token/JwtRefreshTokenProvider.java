package ru.example.ivtserver.security.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class JwtRefreshTokenProvider extends JwtReusableTokenProvider {

    @Value("${security.token.jwt.header-refresh-token}")
    String tokenHeader;

    public JwtRefreshTokenProvider(Environment env) {
        super(env.getRequiredProperty("security.token.jwt.refresh"),
                env.getRequiredProperty("security.token.jwt.valid-time-refresh-second", Long.class));
    }

    @Override
    public Optional<String> getToken(@NonNull HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(tokenHeader));
    }

}
