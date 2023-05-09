package ru.example.ivtserver.exceptions.auth;

/**
 * Исключение, которое выбрасывается, когда указанный токен обновления не существует.
 */
public class NotExistsRefreshTokenException extends RefreshTokenException {

    public NotExistsRefreshTokenException(String message) {
        super(message);
    }

    public NotExistsRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
