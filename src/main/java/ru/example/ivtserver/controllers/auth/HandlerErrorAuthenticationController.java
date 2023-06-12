package ru.example.ivtserver.controllers.auth;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.example.ivtserver.entities.mapper.auth.MessageErrorDto;
import ru.example.ivtserver.exceptions.auth.IncorrectCredentialsException;
import ru.example.ivtserver.exceptions.auth.InvalidDisposableToken;
import ru.example.ivtserver.exceptions.auth.NoUserException;
import ru.example.ivtserver.exceptions.auth.RefreshTokenException;

/**
 * Контролер для обработки ошибок взаимодействия с пользователем
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestControllerAdvice(basePackages = "ru.example.ivtserver.controllers.auth")
public class HandlerErrorAuthenticationController {

    /**
     * Обрабатывает исключения, связанные с некорректными учетными данными
     * @param request Объект {@link HttpServletRequest}, представляющий входящий запрос.
     * @return Объект {@link MessageErrorDto}, содержащий сообщение об ошибке.
     */
    @ExceptionHandler({IncorrectCredentialsException.class, NoUserException.class,
            MethodArgumentNotValidException.class, InvalidDisposableToken.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public MessageErrorDto handlerLogin(HttpServletRequest request) {
        return MessageErrorDto.builder()
                .message("Неверные учетные данные")
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Обрабатывает исключения, связанные с недействительным токеном обновления.
     * @param request Объект {@link HttpServletRequest}, представляющий входящий запрос.
     * @return Объект {@link MessageErrorDto}, содержащий сообщение об ошибке.
     */
    @ExceptionHandler({RefreshTokenException.class, MissingRequestCookieException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public MessageErrorDto handlerRefreshToken(HttpServletRequest request) {
        return MessageErrorDto.builder()
                .message("Неверный формат токена")
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .build();
    }

}
