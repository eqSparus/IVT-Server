package ru.example.ivtserver.services.auth.couch;

import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.email.EmailProvider;
import ru.example.ivtserver.entities.dto.auth.ChangeEmailRequestDto;
import ru.example.ivtserver.entities.dto.auth.ChangePasswordRequestDto;
import ru.example.ivtserver.exceptions.auth.InvalidDisposableToken;
import ru.example.ivtserver.exceptions.auth.NoUserException;
import ru.example.ivtserver.repositories.RefreshTokenRepository;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.security.token.TokenProvider;
import ru.example.ivtserver.services.auth.ChangeParamUserService;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Log4j2
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


    @Override
    public void sendRecoverPassEmail(String email) throws NoUserException {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserException("Нет такого пользователя"));

        var claims = Jwts.claims(Map.of("pass", user.getPassword()))
                .setSubject(user.getEmail());

        var token = disposableTokenProvider.generateToken(claims);

        var message = emailProvider.getMailHtml("password-recovery.html", context -> {
            context.setVariable("token", token);
            return context;
        });

        log.info("Пользователь {}", user.getEmail());
        log.info("Токен для восстановления пароля {}", token);

        emailProvider.sendEmail(email, "Восстановление пароля",
                message, Map.of("logo.png", fileLogoIvt));
    }

    @Override
    public void recoverPassword(String token, String newPassword)
            throws NoUserException, InvalidDisposableToken {

        log.debug("Токен для изменения пароля {}", token);

        if (disposableTokenProvider.isValidToken(token)) {
            var claims = disposableTokenProvider.getBody(token)
                    .orElseThrow(() -> new InvalidDisposableToken("Неверное тело токена"));

            var user = userRepository.findByEmail(claims.getSubject())
                    .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));

            if (claims.get("pass").equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));

                log.debug("Изменения пароля пользователя {}", user.getEmail());

                refreshTokenRepository.deleteAllByUserId(user.getId().toString());
                userRepository.save(user);
            } else {
                throw new InvalidDisposableToken("Токен уже использовался");
            }
        } else {
            throw new InvalidDisposableToken("Неверный одноразовый токен");
        }
    }

    @Override
    public void sendChangeEmail(ChangeEmailRequestDto dto, String email) throws NoUserException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));


        var claims = Jwts.claims(Map.of("new", dto.getEmail())).setSubject(user.getEmail());
        var token = disposableTokenProvider.generateToken(claims);

        log.debug("Пользователь запросивший смену почты {}", email);
        log.debug("Токен для смены почты {}", token);

        var message = emailProvider.getMailHtml("change-email.html", context -> {
            context.setVariable("token", token);
            return context;
        });

        emailProvider.sendEmail(dto.getEmail(), "Изменение электронной почты",
                message, Map.of("logo.png", fileLogoIvt));
    }

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

                log.debug("Старая почта пользователя {}", claims.getSubject());
                log.debug("Новая почта пользователя {}", newEmail);

                user.setEmail(newEmail);
                userRepository.save(user);
            } else {
                throw new InvalidDisposableToken("Токен уже использовался");
            }
        } else {
            throw new InvalidDisposableToken("Неверный одноразовый токен");
        }
    }

    @Override
    public void changePassword(ChangePasswordRequestDto dto, String email)
            throws NoUserException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));
        log.info("Пользователь {} меняет пароль", email);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }

}