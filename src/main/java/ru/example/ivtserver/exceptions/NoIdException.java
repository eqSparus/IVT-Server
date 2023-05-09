package ru.example.ivtserver.exceptions;

/**
 * Исключение, которое выбрасывается, когда идентификатор не найден.
 */
public class NoIdException extends RuntimeException {

    public NoIdException(String message) {
        super(message);
    }

    public NoIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
