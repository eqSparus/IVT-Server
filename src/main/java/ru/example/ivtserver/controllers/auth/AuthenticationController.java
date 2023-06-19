package ru.example.ivtserver.controllers.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.mapper.auth.request.UserRequestDto;
import ru.example.ivtserver.services.auth.UserService;

import java.util.Map;

/**
 * Контроллер для входа, выхода, и обновления пользователя.
 */
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true",
        methods = RequestMethod.POST)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
public class AuthenticationController {

    /**
     * Время жизни токена обновления
     */
    @Value("${security.token.jwt.valid-time-refresh-second}")
    Long cookieLifeTime;

    @Value("${security.token.jwt.cookie-refresh-token}")
    String nameRefreshCookie;

    static final String SAME_SET_COOKIE = "STRICT";

    final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Конечная точка для авторизации пользователя в приложении и установка токена обновления в печенье {@code nameRefreshCookie}.
     *
     * @param userDto  данные о пользователе, которые отправляются в запросе в формате JSON
     * @param response объект ответа {@link HttpServletResponse} для установки cookies
     * @return accessToken для доступа к ресурсам приложения, устанавливает refreshCookie в response
     */
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> login(
            @RequestBody @Valid UserRequestDto userDto,
            HttpServletResponse response
    ) {
        var authentication = userService.login(userDto);
        var refreshCookie = ResponseCookie.from(nameRefreshCookie, authentication.getRefreshToken())
                .httpOnly(true)
                .sameSite(SAME_SET_COOKIE)
                .secure(false)
                .maxAge(cookieLifeTime)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return Map.of(
                "accessToken", authentication.getAccessToken()
        );
    }

    /**
     * Конечная точка для обновления accessToken и печеньки refreshToken
     * при помощи значения refreshToken из печеньки
     *
     * @param refresh  печенька в которой находится токен обновления
     * @param response объект ответа {@link HttpServletResponse} для установки cookies
     * @return accessToken для доступа к ресурсам приложения, устанавливает refreshCookie в response
     */
    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> refreshToken(
            @CookieValue(name = "${security.token.jwt.cookie-refresh-token}") Cookie refresh,
            HttpServletResponse response
    ) {
        var authentication = userService.refreshToken(refresh.getValue());

        var refreshCookie = ResponseCookie.from(nameRefreshCookie, authentication.getRefreshToken())
                .httpOnly(true)
                .sameSite(SAME_SET_COOKIE)
                .secure(false)
                .maxAge(cookieLifeTime)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return Map.of(
                "accessToken", authentication.getAccessToken()
        );
    }

    /**
     * Конечная точка для выхода пользователя из аккаунта путем удаления печеньки {@code nameRefreshCookie}
     * и удаления JWT токена обновления из базы данных
     *
     * @param refresh  печенька в которой находится токен обновления
     * @param response Объект ответа {@link HttpServletResponse} для удаления cookies
     * @return {@link ResponseEntity} с сообщением о выходе
     */
    @PostMapping(path = "/exit")
    public ResponseEntity<String> logout(
            @CookieValue(name = "${security.token.jwt.cookie-refresh-token}") Cookie refresh,
            HttpServletResponse response
    ) {
        userService.logout(refresh.getValue());
        var refreshCookie = ResponseCookie.from(nameRefreshCookie, "")
                .httpOnly(true)
                .sameSite(SAME_SET_COOKIE)
                .secure(false)
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return ResponseEntity.ok("Выход");
    }
}
