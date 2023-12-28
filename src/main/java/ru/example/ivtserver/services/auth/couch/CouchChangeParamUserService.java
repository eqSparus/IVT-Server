package ru.example.ivtserver.services.auth.couch;

import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.email.EmailProvider;
import ru.example.ivtserver.entities.request.auth.ChangeEmailRequest;
import ru.example.ivtserver.entities.request.auth.ChangePasswordRequest;
import ru.example.ivtserver.exceptions.auth.InvalidDisposableToken;
import ru.example.ivtserver.exceptions.auth.NoUserException;
import ru.example.ivtserver.repositories.RefreshTokenRepository;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.security.token.TokenProvider;
import ru.example.ivtserver.services.auth.ChangeParamUserService;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class CouchChangeParamUserService implements ChangeParamUserService {

    final UserRepository userRepository;
    final RefreshTokenRepository refreshTokenRepository;
    final TokenProvider disposableTokenProvider;
    final EmailProvider emailProvider;
    final PasswordEncoder passwordEncoder;

    @Value("classpath:/mail/images/logo-title.png")
    Resource fileLogoIvt;

    public CouchChangeParamUserService(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            @Qualifier("jwtDisposableTokenProvider") TokenProvider disposableTokenProvider,
            EmailProvider emailProvider,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.disposableTokenProvider = disposableTokenProvider;
        this.emailProvider = emailProvider;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Отправляет электронное письмо для восстановления пароля для пользователя с заданным адресом электронной почты.
     * @param email Адрес электронной почты пользователя, который забыл свой пароль.
     * @throws NoUserException бросается если пользователь с указанным адресом электронной почты не найден в базе данных.
     */
    @Async
    @Override
    public void sendRecoverPassEmail(String email) throws NoUserException {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserException("Нет такого пользователя"));

        var claims = Jwts.claims().add(Map.of("pass", user.getPassword()))
                .subject(user.getEmail())
                .build();

        var token = disposableTokenProvider.generateToken(claims);

        var message = emailProvider.getMailHtml("password-recovery.html", context -> {
            context.setVariable("token", token);
            return context;
        });
        emailProvider.sendEmail(email, "Восстановление пароля",
                message, Map.of("logo.png", fileLogoIvt));
    }

    /**
     * Изменяет пароль для пользователя с использованием заданного токена восстановления.
     * @param token Токен, который будет использоваться для восстановления пароля пользователя.
     * @param newPassword Новый пароль, который будет установлен для пользователя.
     * @throws InvalidDisposableToken бросается если заданный токен восстановления недействительный или истек.
     * @throws NoUserException бросается если пользователь, для которого выполняется операция изменения пароля, не найден в базе данных.
     */
    @Override
    public void recoverPassword(String token, String newPassword)
            throws NoUserException, InvalidDisposableToken {

        if (disposableTokenProvider.isValidToken(token)) {
            var claims = disposableTokenProvider.getBody(token)
                    .orElseThrow(() -> new InvalidDisposableToken("Неверное тело токена"));

            var user = userRepository.findByEmail(claims.getSubject())
                    .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));

            if (claims.get("pass").equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));

                refreshTokenRepository.deleteAllByUserId(user.getId().toString());
                userRepository.save(user);
            } else {
                throw new InvalidDisposableToken("Токен уже использовался");
            }
        } else {
            throw new InvalidDisposableToken("Неверный одноразовый токен");
        }
    }

    /**
     * Отправляет электронное письмо для изменения адреса электронной почты для пользователя с заданным адресом электронной почты.
     * @param dto Запрос {@link ChangeEmailRequest} на изменение адреса электронной почты пользователя.
     * @param email Адрес электронной почты пользователя, который хочет изменить свой адрес электронной почты.
     * @throws NoUserException бросается если пользователь с указанным адресом электронной почты не найден в базе данных.
     */
    @Async
    @Override
    public void sendChangeEmail(ChangeEmailRequest dto, String email) throws NoUserException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));


        var claims = Jwts.claims().add(Map.of("new", dto.getEmail()))
                .subject(user.getEmail())
                .build();
        var token = disposableTokenProvider.generateToken(claims);

        var message = emailProvider.getMailHtml("change-email.html", context -> {
            context.setVariable("token", token);
            return context;
        });

        emailProvider.sendEmail(dto.getEmail(), "Изменение электронной почты",
                message, Map.of("logo.png", fileLogoIvt));
    }

    /**
     * Изменяет адрес электронной почты для пользователя с использованием заданного токена изменения адреса электронной почты.
     * @param token Токен, который будет использоваться для изменения адреса электронной почты пользователя.
     * @throws InvalidDisposableToken бросается если заданный токен недействителен или истек.
     * @throws NoUserException бросается если пользователь, для которого выполняется операция изменения адреса электронной почты, не найден в базе данных.
     */
    @Override
    public void changeEmail(String token)
            throws InvalidDisposableToken, NoUserException {
        if (disposableTokenProvider.isValidToken(token)) {

            var claims = disposableTokenProvider.getBody(token)
                    .orElseThrow(() -> new InvalidDisposableToken("Неверное тело токена"));

            var user = userRepository.findByEmail(claims.getSubject())
                    .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));

            if (user.getEmail().equals(claims.getSubject())) {

                var newEmail = claims.get("new").toString();

                user.setEmail(newEmail);
                userRepository.save(user);
            } else {
                throw new InvalidDisposableToken("Токен уже использовался");
            }
        } else {
            throw new InvalidDisposableToken("Неверный одноразовый токен");
        }
    }

    /**
     * Изменяет пароль для пользователя с использованием данных из запроса на изменение пароля.
     * @param dto Запрос на изменение пароля пользователя {@link ChangePasswordRequest}.
     * @param email Адрес электронной почты пользователя, для которого выполняется операция изменения пароля.
     * @throws NoUserException бросается если пользователь, для которого выполняется операция изменения пароля, не найден в базе данных.
     */
    @Override
    public void changePassword(ChangePasswordRequest dto, String email)
            throws NoUserException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }

    /**
     * Проверяет, действителен ли заданный токен для восстановления пароля.
     * @param token Токен для восстановления пароля, который нужно проверить на действительность.
     * @return {@code true}, если токен действителен; {@code false} в противном случае.
     */
    @Override
    public boolean isValidTokenPassword(String token) {
        if (disposableTokenProvider.isValidToken(token)) {
            var claims = disposableTokenProvider.getBody(token)
                    .orElseThrow(() -> new InvalidDisposableToken("Неверное тело токена"));

            var user = userRepository.findByEmail(claims.getSubject())
                    .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));

            return claims.get("pass").equals(user.getPassword());
        }
        return false;
    }

}
