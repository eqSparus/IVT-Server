package ru.example.ivtserver.exceptions.auth;

/**
 * Исключение, которое выбрасывается, когда пользователя, связанного с заданным токеном обновления, не существует.
 */
public class NoUserWithRefreshTokenException extends RefreshTokenException {


    public NoUserWithRefreshTokenException(String message) {
        super(message);
    }

    public NoUserWithRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
