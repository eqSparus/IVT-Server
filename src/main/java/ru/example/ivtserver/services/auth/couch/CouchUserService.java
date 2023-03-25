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
import ru.example.ivtserver.entities.dto.auth.AuthenticationToken;
import ru.example.ivtserver.entities.dto.auth.UserRequestDto;
import ru.example.ivtserver.exceptions.auth.IncorrectCredentialsException;
import ru.example.ivtserver.exceptions.auth.InvalidRefreshTokenException;
import ru.example.ivtserver.exceptions.auth.NoUserWithRefreshTokenException;
import ru.example.ivtserver.exceptions.auth.NotExistsRefreshTokenException;
import ru.example.ivtserver.repositories.RefreshTokenRepository;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.security.token.JwtAuthenticationTokenProvider;
import ru.example.ivtserver.services.auth.UserService;

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

    @Override
    public AuthenticationToken login(UserRequestDto userDao)
            throws IncorrectCredentialsException {

        log.info("Входящий пользователь {}", userDao);
        try {
            var authentication = new UsernamePasswordAuthenticationToken(userDao.getEmail(), userDao.getPassword());
            authenticationManager.authenticate(authentication);

            var user = userRepository.findByEmail(userDao.getEmail())
                    .orElseThrow(IllegalArgumentException::new);

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

            // TODO Удалить при ненадобности
//            refreshTokenRepository.deleteByToken(refreshDto.getToken());

            refreshTokenDb.setToken(newRefreshToken);

//            var refreshTokenDb = RefreshToken.builder()
//                    .userId(user.getId())
//                    .token(newRefreshToken)
//                    .build();
            refreshTokenRepository.save(refreshTokenDb);

            return AuthenticationToken.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        throw new InvalidRefreshTokenException("Неверный токен обновления");
    }

    @Override
    public void logout(String refreshToken) throws InvalidRefreshTokenException {
        if (tokenRefreshProvider.isValidToken(refreshToken)) {
            log.info("Удаление токена обновления {}", refreshToken);

//            try {
//                TimeUnit.MILLISECONDS.sleep(200);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            var token = refreshTokenRepository.findByToken(refreshToken);

            log.info("{}", token);

            refreshTokenRepository.deleteByToken(refreshToken);
        } else {
            throw new InvalidRefreshTokenException("Неверный токен обновления");
        }
    }


}
