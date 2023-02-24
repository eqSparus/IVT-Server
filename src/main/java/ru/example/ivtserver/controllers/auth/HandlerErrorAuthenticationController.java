package ru.example.ivtserver.controllers.auth;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.example.ivtserver.entities.dao.auth.MessageErrorDto;
import ru.example.ivtserver.exceptions.auth.IncorrectCredentialsException;
import ru.example.ivtserver.exceptions.auth.RefreshTokenException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestControllerAdvice(basePackages = "ru.example.ivtserver.controllers.auth")
public class HandlerErrorAuthenticationController {

    @ExceptionHandler({IncorrectCredentialsException.class})
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public MessageErrorDto handlerLogin(HttpServletRequest request) {
        return MessageErrorDto.builder()
                .message("Неверные учетные данные")
                .status(HttpStatus.CONFLICT.value())
                .path(request.getContextPath() + request.getServletPath())
                .build();
    }

    @ExceptionHandler({RefreshTokenException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public MessageErrorDto handlerRefreshToken(HttpServletRequest request) {
        return MessageErrorDto.builder()
                .message("Неверный токен обновления")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getContextPath() + request.getServletPath())
                .build();
    }

    @ExceptionHandler({JwtException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public MessageErrorDto handlerJwtToken(HttpServletRequest request) {
        return MessageErrorDto.builder()
                .message("Неверный формат токена")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getContextPath() + request.getServletPath())
                .build();
    }

}
