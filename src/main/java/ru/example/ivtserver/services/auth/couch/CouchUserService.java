package ru.example.ivtserver.services.auth.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.RefreshToken;
import ru.example.ivtserver.entities.mapper.auth.AuthenticationToken;
import ru.example.ivtserver.entities.mapper.auth.request.UserRequestDto;
import ru.example.ivtserver.exceptions.auth.*;
import ru.example.ivtserver.repositories.RefreshTokenRepository;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.security.token.JwtAuthenticationTokenProvider;
import ru.example.ivtserver.services.auth.UserService;

/**
 * Класс реализующий интерфейс {@link UserService} для работы с учетными данными пользователя.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Log4j2
public class CouchUserService implements UserService {

    UserRepository userRepository;
    RefreshTokenRepository refreshTokenRepository;
    JwtAuthenticationTokenProvider tokenAccessProvider;
    JwtAuthenticationTokenProvider tokenRefreshProvider;
    AuthenticationManager authenticationManager;


    @Autowired
    public CouchUserService(UserRepository userRepository,
                            RefreshTokenRepository refreshTokenRepository,
                            @Qualifier("jwtAccessTokenProvider") JwtAuthenticationTokenProvider tokenAccessProvider,
                            @Qualifier("jwtRefreshTokenProvider") JwtAuthenticationTokenProvider tokenRefreshProvider,
                            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenAccessProvider = tokenAccessProvider;
        this.tokenRefreshProvider = tokenRefreshProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Выполняет процедуру аутентификации пользователя с использованием переданных учетных данных.
     * @param userDao Объект {@link UserRequestDto}, содержащий данные пользователя, который пытается пройти аутентификацию.
     * @return Токены аутентификации представленные {@link AuthenticationToken}.
     * @throws IncorrectCredentialsException Если переданные учетные данные неверны или не найдены.
     * @throws NoUserException бросается если пользователь не был найден.
     */
    @Override
    public AuthenticationToken login(UserRequestDto userDao)
            throws IncorrectCredentialsException, NoUserException {

        log.info("Входящий пользователь {}", userDao);
        try {
            var authentication = new UsernamePasswordAuthenticationToken(userDao.getEmail(), userDao.getPassword());
            authenticationManager.authenticate(authentication);

            var user = userRepository.findByEmail(userDao.getEmail())
                    .orElseThrow(() -> new NoUserException("Пользователь не найден"));

            var refreshToken = tokenRefreshProvider.generateToken(user.getEmail());
            var accessToken = tokenAccessProvider.generateToken(user.getEmail());

            log.debug("Токен доступа {}", accessToken);
            log.debug("Токен обновления {}", refreshToken);

            var refreshTokenDb = RefreshToken.builder()
                    .userId(user.getId())
                    .token(refreshToken)
                    .build();

            refreshTokenRepository.save(refreshTokenDb);

            return AuthenticationToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (BadCredentialsException e) {
            log.debug("Введены неверные учетные данные {}", userDao);
            throw new IncorrectCredentialsException("Введены неверные учетные данные", e);
        }
    }

    /**
     * Обновляет токен доступа и обновления, используя переданный токен обновления.
     * @param refreshToken Токен обновления, который будет использоваться для обновления токенов.
     * @return Новые токены аутентификации, представленные объектом {@link AuthenticationToken}.
     * @throws InvalidRefreshTokenException Если переданный токен обновления недействителен.
     * @throws NotExistsRefreshTokenException Если токен обновления не найден в базе данных.
     * @throws NoUserWithRefreshTokenException Если пользователь с заданным токеном обновления не найден в базе данных.
     */
    @Override
    public AuthenticationToken refreshToken(String refreshToken)
            throws InvalidRefreshTokenException, NotExistsRefreshTokenException, NoUserWithRefreshTokenException {

        log.debug("Токен обновления {}", refreshToken);
        if (tokenRefreshProvider.isValidToken(refreshToken)) {


            var refreshTokenDb = refreshTokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new NotExistsRefreshTokenException("Токена обновления не существует!"));

            var user = userRepository.findById(refreshTokenDb.getUserId())
                    .orElseThrow(() -> new NoUserWithRefreshTokenException("Пользователя с таким токеном не существует"));

            var newAccessToken = tokenAccessProvider.generateToken(user.getEmail());
            var newRefreshToken = tokenRefreshProvider.generateToken(user.getEmail());

            log.debug("Новый токен доступа {}", newAccessToken);
            log.debug("Новый токен обновления {}", newRefreshToken);

            refreshTokenDb.setToken(newRefreshToken);
            refreshTokenRepository.save(refreshTokenDb);

            return AuthenticationToken.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        throw new InvalidRefreshTokenException("Неверный токен обновления");
    }

    /**
     * Осуществляет процедуру выхода из системы для пользователя с указанным токеном обновления.
     * @param refreshToken Токен обновления, который пытается выйти из системы.
     * @throws InvalidRefreshTokenException Если переданный токен обновления недействителен.
     */
    @Override
    public void logout(String refreshToken) throws InvalidRefreshTokenException {
        if (tokenRefreshProvider.isValidToken(refreshToken)) {
            log.info("Удаление токена обновления {}", refreshToken);
            refreshTokenRepository.deleteByToken(refreshToken);
        } else {
            throw new InvalidRefreshTokenException("Неверный токен обновления");
        }
    }
}
