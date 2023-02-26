package ru.example.ivtserver.exceptions.auth;

public class InvalidDisposableToken extends RuntimeException{

    public InvalidDisposableToken(String message) {
        super(message);
    }

    public InvalidDisposableToken(String message, Throwable cause) {
        super(message, cause);
    }
}
