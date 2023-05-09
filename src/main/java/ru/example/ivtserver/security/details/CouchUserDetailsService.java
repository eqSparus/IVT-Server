package ru.example.ivtserver.security.details;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.repositories.UserRepository;

/**
 * Сервис, предназначенный для загрузки пользователей из базы данных Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CouchUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public CouchUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Такого пользователя не существует"));

        return CouchUserDetails.of(user);
    }
}
