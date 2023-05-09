package ru.example.ivtserver.exceptions.auth;

/**
 * Исключение, которое выбрасывается, когда запрашиваемый пользователь не найден.
 */
public class NoUserException extends RuntimeException {

    public NoUserException(String message) {
        super(message);
    }

    public NoUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
