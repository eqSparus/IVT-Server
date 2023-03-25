package ru.example.ivtserver.services.auth;

import ru.example.ivtserver.entities.dto.auth.AuthenticationToken;
import ru.example.ivtserver.entities.dto.auth.UserRequestDto;
import ru.example.ivtserver.exceptions.auth.IncorrectCredentialsException;
import ru.example.ivtserver.exceptions.auth.InvalidRefreshTokenException;
import ru.example.ivtserver.exceptions.auth.NoUserWithRefreshTokenException;
import ru.example.ivtserver.exceptions.auth.NotExistsRefreshTokenException;

public interface UserService {

    AuthenticationToken login(UserRequestDto userDao) throws IncorrectCredentialsException;

    AuthenticationToken refreshToken(String refreshToken)
            throws InvalidRefreshTokenException, NotExistsRefreshTokenException, NoUserWithRefreshTokenException;

    void logout(String refreshToken) throws InvalidRefreshTokenException;

}
