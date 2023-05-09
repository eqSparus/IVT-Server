package ru.example.ivtserver.exceptions.auth;

/**
 * Исключение, которое выбрасывается, когда учетные данные пользователя неверны.
 */
public class IncorrectCredentialsException extends RuntimeException {

    public IncorrectCredentialsException(String message) {
        super(message);
    }

    public IncorrectCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
