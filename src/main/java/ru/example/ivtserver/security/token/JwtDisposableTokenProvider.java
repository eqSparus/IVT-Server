package ru.example.ivtserver.security.token;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.NonNull;

@FieldDefaults(level = AccessLevel.PROTECTED,makeFinal = true)
public abstract class JwtDisposableTokenProvider extends JwtTokenProvider {

    protected JwtDisposableTokenProvider(String secretValue, Long lifeTime) {
        super(secretValue, lifeTime);
    }

    public abstract boolean isTokenNotUsed(@NonNull String token);
}
