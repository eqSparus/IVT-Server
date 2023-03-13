package ru.example.ivtserver.services.auth;

import ru.example.ivtserver.entities.dto.auth.AuthenticationDto;
import ru.example.ivtserver.entities.dto.auth.RefreshTokenRequestDto;
import ru.example.ivtserver.entities.dto.auth.UserRequestDto;
import ru.example.ivtserver.exceptions.auth.IncorrectCredentialsException;
import ru.example.ivtserver.exceptions.auth.InvalidRefreshTokenException;
import ru.example.ivtserver.exceptions.auth.NoUserWithRefreshTokenException;
import ru.example.ivtserver.exceptions.auth.NotExistsRefreshTokenException;

public interface UserService {

    AuthenticationDto login(UserRequestDto userDao) throws IncorrectCredentialsException;

    AuthenticationDto refreshToken(RefreshTokenRequestDto refreshDto)
            throws InvalidRefreshTokenException, NotExistsRefreshTokenException, NoUserWithRefreshTokenException;

    void logout(RefreshTokenRequestDto dto) throws InvalidRefreshTokenException;

}
