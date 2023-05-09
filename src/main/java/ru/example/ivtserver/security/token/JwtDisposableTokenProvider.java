package ru.example.ivtserver.security.token;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Поставщик временных JWT-токенов для восстановления или обновления учетных данных пользователя.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtDisposableTokenProvider extends JwtTokenProvider {

    public JwtDisposableTokenProvider(Environment env) {
        super(env.getRequiredProperty("security.token.jwt.disposable"),
                env.getRequiredProperty("security.token.jwt.valid-time-disposable-second", Long.class));
    }

}
