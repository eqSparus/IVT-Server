package ru.example.ivtserver.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@Log4j2
public class JwtTokenProvider implements TokenProvider {

    Long lifeTime;

    SecretKey key;
    JwtParser parser;

    protected JwtTokenProvider(String secretValue, Long lifeTime) {
        this.key = Keys.hmacShaKeyFor(secretValue.getBytes(StandardCharsets.UTF_8));
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
        this.lifeTime = lifeTime;
    }


    @Override
    public String generateToken(String subject) {
        return generateToken(Jwts.claims().setSubject(subject), lifeTime);
    }


    @Override
    public String generateToken(String subject, Long time) {
        return generateToken(Jwts.claims().setSubject(subject), time);
    }


    @Override
    public String generateToken(Claims claims) {
        return generateToken(claims, lifeTime);
    }


    @Override
    public String generateToken(Claims claims, Long time) {

        var now = LocalDateTime.now();
        var timeStop = now.plusSeconds(time)
                .atZone(ZoneId.systemDefault()).toInstant();

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(timeStop))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean isValidToken(String token) {
        try {
            return !parser
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            log.error("Неверный токен {}", token);
        }
        return false;
    }

    @Override
    public Optional<Claims> getBody(String token) {
        try {
            return Optional.of(parser.parseClaimsJws(token).getBody());
        } catch (JwtException e) {
            log.error("Неверный токен {}", token);
        }
        return Optional.empty();
    }
}
