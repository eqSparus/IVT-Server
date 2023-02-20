package ru.example.ivtserver.security.details;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.repositories.UserRepository;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CouchUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public CouchUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);

        return CouchUserDetails.of(user);
    }
}
