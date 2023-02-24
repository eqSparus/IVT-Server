package ru.example.ivtserver.exceptions.auth;

public abstract class RefreshTokenException extends RuntimeException {

    protected RefreshTokenException(String message) {
        super(message);
    }

    protected RefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
