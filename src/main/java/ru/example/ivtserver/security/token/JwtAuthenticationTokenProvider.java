package ru.example.ivtserver.security.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

/**
 * Поставщик токена JWT. Определяет базовые настройки токена и общую логику обработки получения токена из запроса.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class JwtAuthenticationTokenProvider extends JwtTokenProvider {

    protected JwtAuthenticationTokenProvider(String secretValue, Long lifeTime) {
        super(secretValue, lifeTime);
    }

    /**
     * Получает токен JWT из заданного {@link HttpServletRequest} запроса.
     * @param request HTTP запрос, из которого будет извлекаться токен
     * @return {@link Optional}, содержащий токен JWT, если он присутствует в запросе, и {@link Optional#empty()},
     * если токен отсутствует или не актуален
     */
    public abstract Optional<String> getToken(HttpServletRequest request);
}
