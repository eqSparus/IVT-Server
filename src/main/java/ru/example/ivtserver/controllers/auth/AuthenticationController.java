package ru.example.ivtserver.controllers.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
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

@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RestController
@Log4j2
public class AuthenticationController {

    @Value("${security.token.jwt.valid-time-refresh-second}")
    Long cookieLifeTime;

    static final String SAME_SET_COOKIE = "STRICT";
    static final String NAME_REFRESH_COOKIE = "refresh";

    final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> login(
            @RequestBody @Valid UserRequestDto userDto,
            HttpServletResponse response
    ) {
        var authentication = userService.login(userDto);
        var refreshCookie = ResponseCookie.from(NAME_REFRESH_COOKIE, authentication.getRefreshToken())
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

    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> refreshToken(
            @CookieValue(name = "refresh") Cookie cookie,
            HttpServletResponse response
    ) {
        var authentication = userService.refreshToken(cookie.getValue());

        var refreshCookie = ResponseCookie.from(NAME_REFRESH_COOKIE, authentication.getRefreshToken())
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

    @PostMapping(path = "/exit")
    public ResponseEntity<String> logout(
            @CookieValue(name = "refresh") Cookie cookie,
            HttpServletResponse response
    ) {
        userService.logout(cookie.getValue());
        var refreshCookie = ResponseCookie.from(NAME_REFRESH_COOKIE, "")
                .httpOnly(true)
                .sameSite(SAME_SET_COOKIE)
                .secure(false)
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return ResponseEntity.ok("Выход");
    }
}
