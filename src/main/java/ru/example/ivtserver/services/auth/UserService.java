package ru.example.ivtserver.services.auth;

import org.springframework.lang.NonNull;
import ru.example.ivtserver.entities.dao.auth.AuthenticationDto;
import ru.example.ivtserver.entities.dao.auth.RefreshTokenDto;
import ru.example.ivtserver.entities.dao.auth.UserRequestDto;
import ru.example.ivtserver.exceptions.auth.IncorrectCredentialsException;
import ru.example.ivtserver.exceptions.auth.RefreshTokenException;

public interface UserService {

    @NonNull
    AuthenticationDto login(@NonNull UserRequestDto userDao) throws IncorrectCredentialsException;

    @NonNull
    AuthenticationDto refreshToken(@NonNull RefreshTokenDto refreshDto) throws RefreshTokenException;

    boolean logout(@NonNull RefreshTokenDto dto) throws RefreshTokenException;
}
