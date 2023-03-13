package ru.example.ivtserver.controllers.auth;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.example.ivtserver.entities.dto.auth.AuthenticationDto;
import ru.example.ivtserver.entities.dto.auth.RefreshTokenRequestDto;
import ru.example.ivtserver.entities.dto.auth.UserRequestDto;
import ru.example.ivtserver.services.auth.UserService;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Log4j2
public class AuthenticationController {

    UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthenticationDto login(
            @RequestBody @Valid UserRequestDto userDto
    ) {
        return userService.login(userDto);
    }

    @PostMapping(path = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthenticationDto refreshToken(
            @RequestBody @Valid RefreshTokenRequestDto dto
    ) {
        return userService.refreshToken(dto);
    }

    @PostMapping(path = "/exit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout(
            @RequestBody @Valid RefreshTokenRequestDto dto
    ) {
        userService.logout(dto);
        return ResponseEntity.ok("Выход");
    }
}
