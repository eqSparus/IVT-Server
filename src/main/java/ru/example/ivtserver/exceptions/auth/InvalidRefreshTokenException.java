package ru.example.ivtserver.exceptions.auth;

public class InvalidRefreshTokenException extends RefreshTokenException {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }

    public InvalidRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
