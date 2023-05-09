package ru.example.ivtserver.security.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Поставщик для токенов обновления для аутентификации по JWT.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class JwtRefreshTokenProvider extends JwtAuthenticationTokenProvider {

    @Value("${security.token.jwt.header-refresh-token}")
    String tokenHeader;

    public JwtRefreshTokenProvider(Environment env) {
        super(env.getRequiredProperty("security.token.jwt.refresh"),
                env.getRequiredProperty("security.token.jwt.valid-time-refresh-second", Long.class));
    }

    /**
     * Извлекает токен JWT из заданного {@link HttpServletRequest} запроса.
     * @param request {@link HttpServletRequest} запрос, из которого будет извлекаться токен
     * @return {@link Optional}, содержащий токен JWT, если он присутствует в запросе,
     * и {@link Optional#empty()}, если токен отсутствует или не актуальный
     */
    @Override
    public Optional<String> getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(tokenHeader));
    }

}
