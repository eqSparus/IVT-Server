package ru.example.ivtserver.services.auth.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.RefreshToken;
import ru.example.ivtserver.entities.dto.auth.AuthenticationTokenDto;
import ru.example.ivtserver.entities.request.auth.UserRequest;
import ru.example.ivtserver.exceptions.auth.*;
import ru.example.ivtserver.repositories.RefreshTokenRepository;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.security.token.JwtAuthenticationTokenProvider;
import ru.example.ivtserver.services.auth.UserService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Класс реализующий интерфейс {@link UserService} для работы с учетными данными пользователя.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class CouchUserService implements UserService {

    final UserRepository userRepository;
    final RefreshTokenRepository refreshTokenRepository;
    final JwtAuthenticationTokenProvider tokenAccessProvider;
    final AuthenticationManager authenticationManager;

    @Value("${security.token.jwt.valid-time-refresh-second}")
    Long expirationRefreshToken;

    @Value("${security.token.jwt.valid-time-access-second}")
    Long expirationAccessToken;


    @Autowired
    public CouchUserService(UserRepository userRepository,
                            RefreshTokenRepository refreshTokenRepository,
                            @Qualifier("jwtAccessTokenProvider") JwtAuthenticationTokenProvider tokenAccessProvider,
                            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenAccessProvider = tokenAccessProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Выполняет процедуру аутентификации пользователя с использованием переданных учетных данных.
     *
     * @param userDao Объект {@link UserRequest}, содержащий данные пользователя, который пытается пройти аутентификацию.
     * @return Токены аутентификации представленные {@link AuthenticationTokenDto}.
     * @throws IncorrectCredentialsException Если переданные учетные данные неверны или не найдены.
     * @throws NoUserException               бросается если пользователь не был найден.
     */
    @Override
    public AuthenticationTokenDto login(UserRequest userDao)
            throws IncorrectCredentialsException, NoUserException {

        try {
            var authentication = new UsernamePasswordAuthenticationToken(userDao.getEmail(), userDao.getPassword());
            authenticationManager.authenticate(authentication);

            var user = userRepository.findByEmail(userDao.getEmail())
                    .orElseThrow(() -> new NoUserException("Пользователь не найден"));

            var validTime = Instant.now().plus(expirationAccessToken, ChronoUnit.SECONDS).toEpochMilli();
            var accessToken = tokenAccessProvider.generateToken(user.getEmail(), validTime);

            var refreshToken = UUID.randomUUID().toString();
            var refreshTokenDb = RefreshToken.builder()
                    .userId(user.getId())
                    .token(refreshToken)
                    .expiration(Instant.now().plus(expirationRefreshToken, ChronoUnit.SECONDS).toEpochMilli())
                    .build();

            refreshTokenRepository.save(refreshTokenDb);

            return AuthenticationTokenDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiration(validTime)
                    .build();
        } catch (BadCredentialsException e) {
            throw new IncorrectCredentialsException("Введены неверные учетные данные", e);
        }
    }

    /**
     * Обновляет токен доступа и обновления, используя переданный токен обновления.
     *
     * @param refreshToken Токен обновления, который будет использоваться для обновления токенов.
     * @return Новые токены аутентификации, представленные объектом {@link AuthenticationTokenDto}.
     * @throws NotExistsRefreshTokenException  Если токен обновления не найден в базе данных.
     * @throws NoUserWithRefreshTokenException Если пользователь с заданным токеном обновления не найден в базе данных.
     * @throws ExpiredExpirationRefreshTokenException Если срок жизни токена обновления истек.
     */
    @Override
    public AuthenticationTokenDto refreshToken(String refreshToken)
            throws NotExistsRefreshTokenException, NoUserWithRefreshTokenException, ExpiredExpirationRefreshTokenException {
        var refreshTokenDb = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new NotExistsRefreshTokenException("Токена обновления не существует!"));

        if (refreshTokenDb.getExpiration() >= Instant.now().toEpochMilli()) {
            var user = userRepository.findById(refreshTokenDb.getUserId())
                    .orElseThrow(() -> new NoUserWithRefreshTokenException("Пользователя с таким токеном не существует"));

            var validTime = Instant.now().plus(expirationAccessToken, ChronoUnit.SECONDS).toEpochMilli();
            var newAccessToken = tokenAccessProvider.generateToken(user.getEmail(), validTime);

            var newRefreshToken = UUID.randomUUID().toString();
            refreshTokenDb.setToken(newRefreshToken);
            refreshTokenDb.setExpiration(Instant.now().plus(expirationRefreshToken, ChronoUnit.SECONDS).toEpochMilli());
            refreshTokenRepository.save(refreshTokenDb);

            return AuthenticationTokenDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .expiration(validTime)
                    .build();
        }
        throw new ExpiredExpirationRefreshTokenException("Срок жизни токена обновления истек");
    }

    /**
     * Осуществляет процедуру выхода из системы для пользователя с указанным токеном обновления.
     *
     * @param refreshToken Токен обновления, который пытается выйти из системы.
     * @throws NotExistsRefreshTokenException Если переданный токен обновления недействителен.
     */
    @Override
    public void logout(String refreshToken) throws NotExistsRefreshTokenException {
        var refreshTokenDb = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new NotExistsRefreshTokenException("Токена обновления не существует!"));
        refreshTokenRepository.delete(refreshTokenDb);
    }
}
