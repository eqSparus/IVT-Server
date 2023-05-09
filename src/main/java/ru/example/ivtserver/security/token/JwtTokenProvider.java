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

/**
 * Класс JwtTokenProvider провайдер токенов JWT, который реализует интерфейс {@link TokenProvider}.
 * Класс выполняет операции, связанные с JWT.
 */
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


    /**
     * Генерирует токен JWT на основе заданного subject с временем жизни по умолчанию {@link #lifeTime}.
     * @param subject строка на основе которой генерируется токен
     * @return сгенерированный токен JWT
     */
    @Override
    public String generateToken(String subject) {
        return generateToken(Jwts.claims().setSubject(subject), lifeTime);
    }


    /**
     * Генерирует токен JWT на основе заданного subject с указанным временем жизни в миллисекундах.
     * @param subject строка на основе которой генерируется токен
     * @param time время жизни токена в миллисекундах
     * @return сгенерированный токен JWT
     */
    @Override
    public String generateToken(String subject, Long time) {
        return generateToken(Jwts.claims().setSubject(subject), time);
    }


    /**
     * Генерирует токен JWT на основе заданных {@link Claims} с временем жизни, по умолчанию {@link #lifeTime}.
     * @param claims, которое будет использовано для генерации токена
     * @return сгенерированный токен JWT
     */
    @Override
    public String generateToken(Claims claims) {
        return generateToken(claims, lifeTime);
    }

    /**
     * Генерирует токен JWT на основе заданных claims с временем жизни, указанным в миллисекундах.
     * @param claims, которое будет использовано для генерации токена
     * @param time время жизни токена в миллисекундах
     * @return сгенерированный токен JWT
     */
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

    /**
     * Проверяет, является ли заданный токен JWT действительным.
     * @param token токен JWT для проверки
     * @return true, если токен действительный, false в противном случае
     */
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

    /**
     * Получает тело токена JWT на основе заданного токена.
     * @param token токен JWT для разбора
     * @return {@link Optional}, содержащий payload токена, если токен валидный, и {@link Optional#empty()}, если токен невалидный
     */
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
