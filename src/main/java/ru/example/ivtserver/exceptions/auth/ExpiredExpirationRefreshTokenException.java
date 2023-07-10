package ru.example.ivtserver.exceptions.auth;

public class ExpiredExpirationRefreshTokenException extends RefreshTokenException{
    public ExpiredExpirationRefreshTokenException(String message) {
        super(message);
    }

    public ExpiredExpirationRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
