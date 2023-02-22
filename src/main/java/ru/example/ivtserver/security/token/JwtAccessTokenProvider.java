package ru.example.ivtserver.security.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class JwtAccessTokenProvider extends JwtTokenProvider{

    @Value("${security.token.jwt.valid-time-second}")
    Long tokenValidTime;

    @Value("${security.token.jwt.bearer}")
    String prefixBearer;

    @Value("${security.token.jwt.header-access-token}")
    String tokenHeader;

    public JwtAccessTokenProvider(Environment env) {
        super(env.getRequiredProperty("security.token.jwt.access"));
    }

    @NonNull
    @Override
    public String generateToken(@NonNull String email) {
        return generateToken(email, tokenValidTime);
    }

    @NonNull
    @Override
    public Optional<String> getToken(@NonNull HttpServletRequest request) {
        var header = request.getHeader(tokenHeader);
        if (Objects.nonNull(header) && header.startsWith(prefixBearer)) {
            return Optional.of(header.substring(prefixBearer.length()));
        }
        return Optional.empty();
    }

}
