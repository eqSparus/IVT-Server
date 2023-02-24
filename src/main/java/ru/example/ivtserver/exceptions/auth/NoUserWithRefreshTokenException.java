package ru.example.ivtserver.exceptions.auth;

public class NoUserWithRefreshTokenException extends RefreshTokenException {


    public NoUserWithRefreshTokenException(String message) {
        super(message);
    }

    public NoUserWithRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
