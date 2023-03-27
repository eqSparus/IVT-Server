package ru.example.ivtserver.controllers;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.example.ivtserver.entities.dto.auth.MessageErrorDto;
import ru.example.ivtserver.exceptions.NoIdException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestControllerAdvice(basePackages = "ru.example.ivtserver.controllers")
public class HandlerErrorController {

    @ExceptionHandler({MethodArgumentNotValidException.class, NoIdException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public MessageErrorDto handlerValidDataException(HttpServletRequest request) {
        return MessageErrorDto.builder()
                .message("Неверные данные")
                .path(request.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

}
