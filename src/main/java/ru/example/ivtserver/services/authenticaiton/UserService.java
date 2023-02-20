package ru.example.ivtserver.services.authenticaiton;

import org.springframework.lang.NonNull;
import ru.example.ivtserver.entities.dao.authenticaiton.UserRequestDto;
import ru.example.ivtserver.entities.dao.authenticaiton.AuthenticationDto;

public interface UserService {

    @NonNull
    AuthenticationDto login(@NonNull UserRequestDto userDao);

}
