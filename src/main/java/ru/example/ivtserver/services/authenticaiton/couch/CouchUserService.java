package ru.example.ivtserver.services.authenticaiton.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.User;
import ru.example.ivtserver.entities.dao.authenticaiton.UserRequestDto;
import ru.example.ivtserver.repositories.UserRepository;
import ru.example.ivtserver.services.authenticaiton.UserService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CouchUserService implements UserService {

    UserRepository userRepository;

    @Autowired
    public CouchUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public User login(UserRequestDto userDao) {
        return null;
    }
}
