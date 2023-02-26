package ru.example.ivtserver.services.auth.couch;

import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.email.EmailProvider;
import ru.example.ivtserver.entities.dto.auth.*;
import ru.example.ivtserver.exceptions.auth.*;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.security.token.JwtAuthenticationTokenProvider;
import ru.example.ivtserver.security.token.TokenProvider;
import ru.example.ivtserver.services.auth.UserService;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Log4j2
public class CouchUserService implements UserService {

    UserRepository userRepository;
    JwtAuthenticationTokenProvider tokenAccessProvider;
    JwtAuthenticationTokenProvider tokenRefreshProvider;

    TokenProvider tokenRecoverPasswordProvider;
    AuthenticationManager authenticationManager;
    EmailProvider emailProvider;
    PasswordEncoder passwordEncoder;


    @Autowired
    public CouchUserService(UserRepository userRepository,
                            @Qualifier("jwtAccessTokenProvider") JwtAuthenticationTokenProvider tokenAccessProvider,
                            @Qualifier("jwtRefreshTokenProvider") JwtAuthenticationTokenProvider tokenRefreshProvider,
                            @Qualifier("jwtDisposableTokenProvider") TokenProvider tokenRecoverPasswordProvider,
                            AuthenticationManager authenticationManager,
                            EmailProvider emailProvider,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenAccessProvider = tokenAccessProvider;
        this.tokenRefreshProvider = tokenRefreshProvider;
        this.tokenRecoverPasswordProvider = tokenRecoverPasswordProvider;
        this.authenticationManager = authenticationManager;
        this.emailProvider = emailProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @NonNull
    @Override
    public AuthenticationDto login(@NonNull UserRequestDto userDao)
            throws IncorrectCredentialsException {

        log.info("Входящий пользователь {}", userDao);
        try {
            var authentication = new UsernamePasswordAuthenticationToken(userDao.getEmail(), userDao.getPassword());
            authenticationManager.authenticate(authentication);

            var user = userRepository.findByEmail(userDao.getEmail())
                    .orElseThrow(IllegalArgumentException::new);

            var token = tokenAccessProvider.generateToken(user.getEmail());
            var refreshToken = tokenRefreshProvider.generateToken(user.getEmail());

            log.debug("Токен доступа {}", token);
            log.debug("Токен обновления {}", refreshToken);

            user.getRefreshTokens().add(refreshToken);
            userRepository.save(user);

            return AuthenticationDto.builder()
                    .accessToken(token)
                    .refreshToken(refreshToken)
                    .build();
        } catch (BadCredentialsException e) {
            if (log.isDebugEnabled()) {
                log.debug("Введены неверные учетные данные {}", userDao);
            }
            throw new IncorrectCredentialsException("Введены неверные учетные данные", e);
        }
    }

    @NonNull
    @Override
    public AuthenticationDto refreshToken(@NonNull RefreshTokenDto refreshDto)
            throws InvalidRefreshTokenException, NotExistsRefreshTokenException, NoUserWithRefreshTokenException {

        log.info("Запрос на обновления токена доступа {}", refreshDto.getToken());
        if (tokenRefreshProvider.isValidToken(refreshDto.getToken())) {

            var claim = tokenRefreshProvider.getBody(refreshDto.getToken())
                    .orElseThrow(() -> new InvalidRefreshTokenException("Неверное тело токена"));

            var user = userRepository.findByEmail(claim.getSubject())
                    .map(u -> {
                        if (!u.getRefreshTokens().contains(refreshDto.getToken())) {
                            throw new NotExistsRefreshTokenException(
                                    "У пользователя не существует такого токена обновления");
                        }
                        return u;
                    })
                    .orElseThrow(() -> new NoUserWithRefreshTokenException("Пользователя токена не существует"));

            var newAccessToken = tokenAccessProvider.generateToken(user.getEmail());
            var newRefreshToken = tokenRefreshProvider.generateToken(user.getEmail());


            log.debug("Новый токен доступа {}", newAccessToken);
            log.debug("Новый токен обновления {}", newRefreshToken);


            user.getRefreshTokens().remove(refreshDto.getToken());
            user.getRefreshTokens().add(newRefreshToken);
            userRepository.save(user);

            return AuthenticationDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        throw new InvalidRefreshTokenException("Неверный токен обновления");
    }

    @Override
    public void logout(@NonNull RefreshTokenDto dto) throws InvalidRefreshTokenException {

        if (tokenRefreshProvider.isValidToken(dto.getToken())) {
            var claim = tokenRefreshProvider.getBody(dto.getToken())
                    .orElseThrow(() -> new InvalidRefreshTokenException("Неверное тело токена"));


            userRepository.findByEmail(claim.getSubject())
                    .ifPresent(u -> {
                        log.debug("Выход пользователя {}", u.getEmail());
                        log.debug("Удаление токена обновления {}", dto.getToken());
                        u.getRefreshTokens().remove(dto.getToken());
                        userRepository.save(u);
                    });
        } else {
            throw new InvalidRefreshTokenException("Неверный токен обновления");
        }
    }

    @Override
    public void sendRecoverPassEmail(@NonNull String email) throws NoUserException {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserException("Нет такого пользователя"));

        var claims = Jwts.claims(Map.of("pass", user.getPassword()))
                .setSubject(user.getEmail());

        var token = tokenRecoverPasswordProvider.generateToken(claims);

        var message = emailProvider.getMailHtml("password-recovery.html", context -> {
            context.setVariable("token", token);
            return context;
        });

        log.debug("Пользователь {}", user.getEmail());
        log.debug("Токен для восстановления пароля {}", token);

        emailProvider.sendEmail(email, "Восстановление пароля", message);
    }

    @Override
    public void recoverPassword(@NonNull String token, @NonNull String newPassword)
            throws NoUserException, InvalidDisposableToken {

        log.debug("Токен для изменения пароля {}", token);

        if (tokenRecoverPasswordProvider.isValidToken(token)) {
            var claims = tokenRecoverPasswordProvider.getBody(token)
                    .orElseThrow(() -> new InvalidDisposableToken("Неверное тело токена"));

            var user = userRepository.findByEmail(claims.getSubject())
                    .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));

            if (claims.get("pass").equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));

                log.debug("Изменения пароля пользователя {}", user.getEmail());

                userRepository.save(user);
            } else {
                throw new InvalidDisposableToken("Токен уже использовался");
            }
        } else {
            throw new InvalidDisposableToken("Неверный одноразовый токен");
        }
    }

    @Override
    public void sendChangeEmail(@NonNull ChangeEmailDto dto, @NonNull String email) throws NoUserException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));


        var claims = Jwts.claims(Map.of("new", dto.getEmail())).setSubject(user.getEmail());
        var token = tokenRecoverPasswordProvider.generateToken(claims);

        var message = emailProvider.getMailHtml("change-email.html", context -> {
            context.setVariable("token", token);
            return context;
        });

        emailProvider.sendEmail(dto.getEmail(), "Изменение электронной почты", message);
    }

    @Override
    public void changeEmail(String token)
            throws InvalidDisposableToken, NoUserException {
        if (tokenRecoverPasswordProvider.isValidToken(token)) {

            var claims = tokenRecoverPasswordProvider.getBody(token)
                    .orElseThrow(() -> new InvalidDisposableToken("Неверное тело токена"));

            var user = userRepository.findByEmail(claims.getSubject())
                    .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));

            if (user.getEmail().equals(claims.getSubject())) {
                user.setEmail(claims.get("new").toString());
                userRepository.save(user);
            } else {
                throw new InvalidDisposableToken("Токен уже использовался");
            }
        } else {
            throw new InvalidDisposableToken("Неверный одноразовый токен");
        }
    }

    @Override
    public void changePassword(@NonNull ChangePasswordDto dto, @NonNull String email)
            throws NoUserException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoUserException("Пользователя с такой почтой не существует"));
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }
}
