package ru.example.ivtserver.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Log4j2
public class JwtTokenProvider implements AccessTokenProvider {

    @Value("${security.token.jwt.valid-time-second}")
    Long tokenValidTime;

    @Value("${security.token.jwt.bearer}")
    String prefixBearer;

    @Value("${security.token.jwt.header-authorization}")
    String tokenHeader;

    final SecretKey key;

    public JwtTokenProvider(Environment env) {
        this.key = Keys.hmacShaKeyFor(env.getRequiredProperty("security.token.jwt.access")
                .getBytes(StandardCharsets.UTF_8));
    }

    @NonNull
    @Override
    public String generateToken(@NonNull String email) {
        return generateToken(email, tokenValidTime);
    }

    @NonNull
    @Override
    public String generateToken(@NonNull String email, @NonNull Long time) {

        var now = LocalDateTime.now();
        var timeStop = now.plusSeconds(time)
                .atZone(ZoneId.systemDefault()).toInstant();

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(Date.from(timeStop))
                .signWith(key)
                .compact();
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

    @Override
    public boolean isValidToken(@NonNull String token) {

        var parser = Jwts.parserBuilder()
                .setSigningKey(key).build();

        try {
            return !parser
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            log.error("Время жизни токена истекло", e);
        } catch (MalformedJwtException e) {
            log.error("Неправильный формат токена", e);
        }

        return false;
    }

    @NonNull
    @Override
    public String getEmailFromToken(@NonNull String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @NonNull
    @Override
    public Claims getBody(@NonNull String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }
}
