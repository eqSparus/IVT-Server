package ru.example.ivtserver.controllers.auth;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.dto.auth.ChangeEmailRequestDto;
import ru.example.ivtserver.entities.dto.auth.ChangePasswordRequestDto;
import ru.example.ivtserver.entities.dto.auth.RecoverPasswordRequestDto;
import ru.example.ivtserver.services.auth.ChangeParamUserService;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class ChangeParamUserController {

    ChangeParamUserService changeParamUserService;


    @Autowired
    public ChangeParamUserController(ChangeParamUserService changeParamUserService) {
        this.changeParamUserService = changeParamUserService;
    }

    @PostMapping(path = "/recover/pass", params = {"email"})
    public ResponseEntity<String> sendRecoverPasswordEmail(
            @RequestParam(name = "email") String email
    ) {
        changeParamUserService.sendRecoverPassEmail(email);
        return ResponseEntity.ok("Отправка письма восстановления пароля");
    }


    @PostMapping(path = "/recover/pass", consumes = MediaType.APPLICATION_JSON_VALUE, params = {"token"})
    public ResponseEntity<String> restorePassword(
            @RequestParam(name = "token") String token,
            @RequestBody @Valid RecoverPasswordRequestDto dto
    ) {
        changeParamUserService.recoverPassword(token, dto.getPassword());
        return ResponseEntity.ok("Пароль изменен");
    }

    @PostMapping(path = "/change/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendChangeEmail(
            @RequestBody @Valid ChangeEmailRequestDto dto,
            Principal user
    ) {
        changeParamUserService.sendChangeEmail(dto, user.getName());
        return ResponseEntity.ok("Отправка письма изменения почты");
    }

    @PostMapping(path = "/change/email", params = {"token"})
    public ResponseEntity<String> changeEmail(
            @RequestParam(name = "token") String token
    ) {
        changeParamUserService.changeEmail(token);
        return ResponseEntity.ok("Изменение адреса");
    }

    @PostMapping(path = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid ChangePasswordRequestDto dto,
            Principal user
    ) {
        changeParamUserService.changePassword(dto, user.getName());
        return ResponseEntity.ok("Изменения пароля");
    }
}
