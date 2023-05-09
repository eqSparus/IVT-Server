package ru.example.ivtserver.exceptions.auth;

/**
 * Исключение, которое выбрасывается, когда использован недействительный временный токен.
 */
public class InvalidDisposableToken extends RuntimeException {

    public InvalidDisposableToken(String message) {
        super(message);
    }

    public InvalidDisposableToken(String message, Throwable cause) {
        super(message, cause);
    }
}
