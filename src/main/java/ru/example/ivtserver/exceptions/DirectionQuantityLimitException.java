package ru.example.ivtserver.exceptions;

public class DirectionQuantityLimitException extends RuntimeException {

    public DirectionQuantityLimitException(String message) {
        super(message);
    }

    public DirectionQuantityLimitException(Throwable cause) {
        super(cause);
    }
}
