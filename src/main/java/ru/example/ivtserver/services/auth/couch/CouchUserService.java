package ru.example.ivtserver.services.auth.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.dao.auth.AuthenticationDto;
import ru.example.ivtserver.entities.dao.auth.RefreshTokenDto;
import ru.example.ivtserver.entities.dao.auth.UserRequestDto;
import ru.example.ivtserver.exceptions.auth.*;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.security.token.TokenProvider;
import ru.example.ivtserver.services.auth.UserService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Log4j2
public class CouchUserService implements UserService {

    UserRepository userRepository;
    TokenProvider tokenAccessProvider;
    TokenProvider tokenRefreshProvider;
    AuthenticationManager authenticationManager;


    @Autowired
    public CouchUserService(UserRepository userRepository,
                            @Qualifier("jwtAccessTokenProvider") TokenProvider tokenAccessProvider,
                            @Qualifier("jwtRefreshTokenProvider") TokenProvider tokenRefreshProvider,
                            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenAccessProvider = tokenAccessProvider;
        this.tokenRefreshProvider = tokenRefreshProvider;
        this.authenticationManager = authenticationManager;
    }

    @NonNull
    @Override
    public AuthenticationDto login(@NonNull UserRequestDto userDao)
            throws IncorrectCredentialsException {

        try {
            var authentication = new UsernamePasswordAuthenticationToken(userDao.getEmail(), userDao.getPassword());
            authenticationManager.authenticate(authentication);

            var user = userRepository.findByEmail(userDao.getEmail())
                    .orElseThrow(IllegalArgumentException::new);

            var token = tokenAccessProvider.generateToken(user.getEmail());
            var refreshToken = tokenRefreshProvider.generateToken(user.getEmail());

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
            throws RefreshTokenException {

        var email = tokenRefreshProvider.getBody(refreshDto.getToken())
                .orElseThrow(() -> new InvalidRefreshTokenException("Неверный токен обновления"));
        var opUser = userRepository.findByEmail(email.getSubject());

        if (log.isDebugEnabled()) {
            log.debug("Найденный пользователь {}", opUser);
        }

        if (opUser.isPresent()) {

            var user = opUser.get();

            if (!user.getRefreshTokens().contains(refreshDto.getToken())) {
                if (log.isDebugEnabled()) {
                    log.debug("Токен {} не существует у пользователя {}", refreshDto.getToken(), user.getEmail());
                }
                throw new NotExistsRefreshTokenException("У пользователя не существует такого токена обновления");
            }

            var newAccessToken = tokenAccessProvider.generateToken(user.getEmail());
            var newRefreshToken = tokenRefreshProvider.generateToken(user.getEmail());

            if (log.isDebugEnabled()) {
                log.debug("Новый токен доступа {}", newAccessToken);
                log.debug("Новый токен обновления {}", newRefreshToken);
            }

            user.getRefreshTokens().remove(refreshDto.getToken());
            user.getRefreshTokens().add(newRefreshToken);
            userRepository.save(user);

            return AuthenticationDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();

        }
        if (log.isDebugEnabled()) {
            log.debug("Пользователя {} с таким токеном {} не существует", refreshDto.getToken());
        }
        throw new NoUserWithRefreshTokenException("Пользователя с таким токеном обновления не существует");
    }

    @Override
    public boolean logout(@NonNull RefreshTokenDto dto) throws RefreshTokenException {

        var email = tokenRefreshProvider.getBody(dto.getToken())
                .orElseThrow(() -> new InvalidRefreshTokenException("Неверный токен обновления"));

        var opUser = userRepository.findByEmail(email.getSubject());

        if (opUser.isPresent()) {
            var user = opUser.get();
            user.getRefreshTokens().remove(dto.getToken());
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
