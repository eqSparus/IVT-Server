package ru.example.ivtserver.security.token;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Поставщик временных JWT-токенов для восстановления или обновления учетных данных пользователя.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtDisposableTokenProvider extends JwtTokenProvider {

    public JwtDisposableTokenProvider(@Value("${security.token.jwt.disposable}") String disposable,
                                      @Value("${security.token.jwt.valid-time-disposable-second}") Long validTime) {
        super(disposable, validTime);
    }

}
