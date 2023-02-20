package ru.example.ivtserver.services.authenticaiton.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.dao.authenticaiton.AuthenticationDto;
import ru.example.ivtserver.entities.dao.authenticaiton.UserRequestDto;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.security.token.TokenCollector;
import ru.example.ivtserver.services.authenticaiton.UserService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Log4j2
public class CouchUserService implements UserService {

    UserRepository userRepository;
    TokenCollector tokenCollector;
    AuthenticationManager authenticationManager;


    @Autowired
    public CouchUserService(UserRepository userRepository,
                            TokenCollector tokenCollector,
                            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenCollector = tokenCollector;
        this.authenticationManager = authenticationManager;
    }

    @NonNull
    @Override
    public AuthenticationDto login(@NonNull UserRequestDto userDao) {

        try {
            var authentication = new UsernamePasswordAuthenticationToken(userDao.getEmail(), userDao.getPassword());
            authenticationManager.authenticate(authentication);

            var user = userRepository.findByEmail(userDao.getEmail())
                    .orElseThrow(IllegalArgumentException::new);


            var token = tokenCollector.generateToken(user.getEmail());

            return AuthenticationDto.builder()
                    .authorization(token)
                    .refreshToken(token)
                    .build();

        } catch (BadCredentialsException e) {
            log.info("Неверные учетные данные!");
            throw new RuntimeException(e);
        }
    }
}
