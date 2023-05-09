package ru.example.ivtserver.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.Optional;

/**
 * Интерфейс, предоставляющий методы для создания и проверки JWT-токенов.
 */
public interface TokenProvider {

    /**
     * Генерирует JWT-токен для указанного субъекта.
     * @param subject субъект, для которого генерируется токен.
     * @return строковое представление JWT токена.
     */
    String generateToken(String subject);

    /**
     * Генерирует JWT-токен для указанного субъекта с заданным временем жизни.
     * @param subject субъект, для которого генерируется токен.
     * @param time время жизни токена, в миллисекундах.
     * @return строковое представление JWT токена.
     */
    String generateToken(String subject, Long time);

    /**
     * Генерирует JWT-токен на основе заданных данных.
     * @param claims данные, которые будут добавлены в токен.
     * @return строковое представление JWT токена.
     */
    String generateToken(Claims claims);

    /**
     * Генерирует JWT-токен на основе заданных данных с заданным временем жизни.
     * @param claims данные, которые будут добавлены в токен.
     * @param time   время жизни токена, в миллисекундах.
     * @return строковое представление JWT токена.
     */
    String generateToken(Claims claims, Long time);

    /**
     * Проверяет, является ли переданный токен допустимым.
     * @param token токен для проверки
     * @return true, если токен допустимый, false в противном случае
     */
    boolean isValidToken(String token);

    /**
     * Возвращает тело JWT, расшифрованное из переданного токена.
     * @param token токен для расшифровки
     * @return опциональный объект Claims с расшифрованными данными из токена
     * @throws JwtException если произошла ошибка при работе с JWT
     */
    Optional<Claims> getBody(String token) throws JwtException;
}
