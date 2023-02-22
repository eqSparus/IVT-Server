package ru.example.ivtserver.controllers.authentication;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.example.ivtserver.entities.User;
import ru.example.ivtserver.entities.UserRole;
import ru.example.ivtserver.entities.dao.authenticaiton.AuthenticationDto;
import ru.example.ivtserver.entities.dao.authenticaiton.UserRequestDto;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.services.authenticaiton.UserService;

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
    @ResponseStatus(code = HttpStatus.OK)
    public AuthenticationDto login(
            @RequestBody UserRequestDto userDto
    ) {
        log.info("Входящий пользователь {}", userDto);
        var dto = userService.login(userDto);
        log.info("Токены доступа {}", dto.getAccessToken());
        log.info("Токены обновления {}", dto.getRefreshToken());
        return dto;
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
