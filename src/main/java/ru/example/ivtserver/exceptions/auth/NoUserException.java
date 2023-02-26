package ru.example.ivtserver.exceptions.auth;

public class NoUserException extends RuntimeException {

    public NoUserException(String message) {
        super(message);
    }

    public NoUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
