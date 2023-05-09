package ru.example.ivtserver.controllers.auth;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.mapper.auth.request.ChangeEmailRequestDto;
import ru.example.ivtserver.entities.mapper.auth.request.ChangePasswordRequestDto;
import ru.example.ivtserver.services.auth.ChangeParamUserService;

import java.security.Principal;

/**
 * Контролер для изменения пароля и почты пользователя, а также восстановления пароля.
 */
@CrossOrigin(origins = "http://localhost:8081", methods = RequestMethod.POST)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class ChangeParamUserController {

    ChangeParamUserService changeParamUserService;


    @Autowired
    public ChangeParamUserController(ChangeParamUserService changeParamUserService) {
        this.changeParamUserService = changeParamUserService;
    }

    /**
     * Конечная точка для отправки электронного письма с запросом на восстановление пароля.
     * @param email Адрес электронной почты, переданный в URL запроса.
     * @return Объект {@link ResponseEntity} с "OK" статусом и сообщением об отправке письма восстановления пароля.
     */
    @PostMapping(path = "/recover/pass", params = {"email"})
    public ResponseEntity<String> sendRecoverPasswordEmail(
            @RequestParam(name = "email") String email
    ) {
        changeParamUserService.sendRecoverPassEmail(email);
        return ResponseEntity.ok("Отправка письма восстановления пароля");
    }


    /**
     * Конечная точка для восстановления пароля пользователя.
     * @param token Токен, переданный в URL запроса.
     * @param dto Тело запроса, содержащее новый пароль.
     * @return Объект {@link ResponseEntity} с "OK" статусом и сообщением о том, что пароль был изменен.
     */
    @PostMapping(path = "/recover/pass", consumes = MediaType.APPLICATION_JSON_VALUE, params = {"token"})
    public ResponseEntity<String> restorePassword(
            @RequestParam(name = "token") String token,
            @RequestBody @Valid ChangePasswordRequestDto dto
    ) {
        changeParamUserService.recoverPassword(token, dto.getPassword());
        return ResponseEntity.ok("Пароль изменен");
    }

    /**
     * Конечная точка для проверки токена на актуальность.
     * @param token Токен, переданный в URL запроса.
     * @return Объект {@link ResponseEntity} с "OK" статусом и логическим значением, указывающим на действительность токена.
     */
    @PostMapping(path = "/recover/pass/valid", params = {"token"})
    public ResponseEntity<Boolean> isValidToken(
            @RequestParam(name = "token") String token
    ) {
        return ResponseEntity.ok(changeParamUserService.isValidTokenPassword(token));
    }

    /**
     * Конечная точка для отправки письма об изменение адреса электронной почты.
     * @param dto Объект {@link ChangeEmailRequestDto}, содержащий новый адрес электронной почты.
     * @param user Объект {@link Principal}, представляющий текущего авторизованного пользователя.
     * @return Объект {@link ResponseEntity} с "OK" статусом и сообщением об отправке письма изменения почты.
     */
    @PostMapping(path = "/change/email", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendChangeEmail(
            @RequestBody @Valid ChangeEmailRequestDto dto,
            Principal user
    ) {
        changeParamUserService.sendChangeEmail(dto, user.getName());
        return ResponseEntity.ok("Отправка письма изменения почты");
    }

    /**
     * Конечная точка для изменения адреса электронной почты.
     * @param token Токен, переданный в URL запроса.
     * @return Объект {@link ResponseEntity} с "OK" статусом и сообщением об изменении адреса электронной почты.
     */
    @PostMapping(path = "/change/email", params = {"token"})
    public ResponseEntity<String> changeEmail(
            @RequestParam(name = "token") String token
    ) {
        changeParamUserService.changeEmail(token);
        return ResponseEntity.ok("Изменение адреса");
    }

    /**
     * Конечная точка для изменения пароля пользователя.
     * @param dto Запрос на изменение пароля, представленный как объект {@link ChangePasswordRequestDto}.
     * @param user Объект {@link Principal}, представляющий текущего пользователя.
     * @return Объект {@link ResponseEntity} с "OK" статусом и сообщением об изменении пароля.
     */
    @PostMapping(path = "/change/password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> changePassword(
            @RequestBody @Valid ChangePasswordRequestDto dto,
            Principal user
    ) {
        changeParamUserService.changePassword(dto, user.getName());
        return ResponseEntity.ok("Изменения пароля");
    }
}
