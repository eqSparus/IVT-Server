package ru.example.ivtserver.security.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@Log4j2
public abstract class JwtTokenProvider implements TokenProvider {

    SecretKey key;
    JwtParser parser;

    protected JwtTokenProvider(String secretValue) {
        this.key = Keys.hmacShaKeyFor(secretValue.getBytes(StandardCharsets.UTF_8));
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
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

    @Override
    public boolean isValidToken(@NonNull String token) {
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
        } catch (SignatureException e){
            log.error("Подпись не подтверждена", e);
        }

        return false;
    }

    @NonNull
    @Override
    public Claims getBody(@NonNull String token) {
        return parser.parseClaimsJwt(token)
                .getBody();
    }

    @NonNull
    @Override
    public String getEmailFromToken(@NonNull String token) {
        return parser.parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
