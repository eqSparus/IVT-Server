package ru.example.ivtserver.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.example.ivtserver.entities.mapper.auth.MessageErrorDto;
import ru.example.ivtserver.exceptions.DirectionQuantityLimitException;
import ru.example.ivtserver.exceptions.NoIdException;

/**
 * Контролер для обработки ошибок настройки наполнения сайта.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestControllerAdvice(basePackages = "ru.example.ivtserver.controllers")
public class HandlerErrorController {

    /**
     * Обрабатывает исключения типа {@link MethodArgumentNotValidException}, {@link NoIdException}, {@link ConversionFailedException}
     * и {@link ConstraintViolationException}, которые могут возникнуть при передаче недопустимых данных, и возвращает
     * объект типа {@link MessageErrorDto} с подробным сообщением об ошибке.
     *
     * @param request Объект типа {@link HttpServletRequest}, представляющий запрос, при выполнении которого возникло исключение.
     * @return Объект типа {@link MessageErrorDto}, содержащий подробное сообщение об ошибке, адрес запроса и идентификатор ошибки.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, NoIdException.class,
            ConversionFailedException.class, ConstraintViolationException.class, DirectionQuantityLimitException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public MessageErrorDto handlerValidDataException(HttpServletRequest request) {
        return MessageErrorDto.builder()
                .message("Неверные данные")
                .path(request.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}
