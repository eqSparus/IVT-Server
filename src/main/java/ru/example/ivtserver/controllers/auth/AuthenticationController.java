package ru.example.ivtserver.controllers.auth;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.User;
import ru.example.ivtserver.entities.UserRole;
import ru.example.ivtserver.entities.dto.auth.*;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.services.auth.UserService;

import java.security.Principal;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@Log4j2
public class AuthenticationController {

    UserService userService;
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    @Autowired
    public AuthenticationController(UserService userService,
                                    PasswordEncoder passwordEncoder,
                                    UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthenticationDto login(
            @RequestBody UserRequestDto userDto
    ) {
        return userService.login(userDto);
    }

    @PostMapping(path = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthenticationDto refreshToken(
            @RequestBody RefreshTokenDto dto
    ) {
        return userService.refreshToken(dto);
    }

    @PostMapping(path = "/exit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout(
            @RequestBody RefreshTokenDto dto
    ) {
        userService.logout(dto);
        return ResponseEntity.ok("Выход");
    }

    @PostMapping(path = "/reset/pass", params = {"email"})
    public ResponseEntity<String> sendRecoverPasswordEmail(
            @RequestParam(name = "email") String email
    ) {
        userService.sendRecoverPassEmail(email);
        return ResponseEntity.ok("Отправка письма восстановления пароля");
    }


    @PostMapping(path = "/reset/pass/{token}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> restorePassword(
            @PathVariable(name = "token") String token,
            @RequestBody RecoverPasswordDto dto
    ) {
        userService.recoverPassword(token, dto.getPassword());
        return ResponseEntity.ok("Пароль изменен");
    }

    @PostMapping(path = "/change/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendChangeEmail(
            @RequestBody ChangeEmailDto dto,
            Principal user
    ) {
        userService.sendChangeEmail(dto, user.getName());
        return ResponseEntity.ok("Отправка письма изменения почты");
    }

    @PostMapping(path = "/change/email/{token}")
    public ResponseEntity<String> changeEmail(
            @PathVariable(name = "token") String token
    ) {
        userService.changeEmail(token);
        return ResponseEntity.ok("Изменение адреса");
    }

    @PostMapping(path = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordDto dto,
            Principal user
    ) {
        userService.changePassword(dto, user.getName());
        return ResponseEntity.ok("Изменения пароля");
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> create() {
        var user = User.builder()
                .email("sparus-1693@yandex.ru")
                .password(passwordEncoder.encode("rootrootroot"))
                .role(UserRole.ADMINISTRATOR)
                .build();

        userRepository.save(user);
        log.info("Пользователь {}", user);

        return ResponseEntity.ok("Создан администратор");
    }

}
