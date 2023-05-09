package ru.example.ivtserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.example.ivtserver.entities.mapper.auth.MessageErrorDto;

import java.io.IOException;

/**
 * Реализация {@link AuthenticationEntryPoint}, которая возвращает сообщение об ошибке при отсутствии
 * аутентификации пользователя.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Log4j2
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    ObjectMapper objectMapper;

    @Autowired
    public AppAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(401);

        var bodyResponse = MessageErrorDto.builder()
                .status(401)
                .message("Для доступа требуется аутентификация")
                .path(request.getRequestURI())
                .build();

        objectMapper.writeValue(response.getOutputStream(), bodyResponse);
    }
}
