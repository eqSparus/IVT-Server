package ru.example.ivtserver.exceptions.auth;

/**
 * Абстрактный класс исключения, которое выбрасывается, когда происходит ошибка токена обновления.
 */
public abstract class RefreshTokenException extends RuntimeException {

    protected RefreshTokenException(String message) {
        super(message);
    }

    protected RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
