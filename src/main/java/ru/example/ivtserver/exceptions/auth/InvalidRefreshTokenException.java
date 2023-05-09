package ru.example.ivtserver.exceptions.auth;

/**
 * Исключение, которое выбрасывается, когда токен обновления недействителен.
 */
public class InvalidRefreshTokenException extends RefreshTokenException {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }

    public InvalidRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
