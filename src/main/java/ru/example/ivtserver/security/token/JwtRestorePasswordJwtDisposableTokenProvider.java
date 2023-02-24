package ru.example.ivtserver.security.token;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class JwtRestorePasswordJwtDisposableTokenProvider extends JwtDisposableTokenProvider {

    protected JwtRestorePasswordJwtDisposableTokenProvider(Environment env) {
        super(env.getRequiredProperty("security.token.jwt.disposable"),
                env.getRequiredProperty("security.token.jwt.valid-time-disposable-second", Long.class));
    }

    @Override
    public boolean isTokenNotUsed(String token) {
        return false;
    }

}
