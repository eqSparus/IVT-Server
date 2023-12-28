package ru.example.ivtserver.exceptions;

/**
 * Исключение, которое выбрасывается, когда произошла ошибка при операции с файлом.
 */
public class FailedOperationFileException extends RuntimeException {

    public FailedOperationFileException(String message) {
        super(message);
    }

    public FailedOperationFileException(Throwable cause) {
        super(cause);
    }
}
