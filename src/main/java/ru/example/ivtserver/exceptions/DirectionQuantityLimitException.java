package ru.example.ivtserver.exceptions;

/**
 * Исключение, которое выбрасывается, когда превышено количество направлений.
 */
public class DirectionQuantityLimitException extends RuntimeException {

    public DirectionQuantityLimitException(String message) {
        super(message);
    }

    public DirectionQuantityLimitException(Throwable cause) {
        super(cause);
    }
}
