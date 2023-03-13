package ru.example.ivtserver.services.auth;

import ru.example.ivtserver.entities.dto.auth.ChangeEmailRequestDto;
import ru.example.ivtserver.entities.dto.auth.ChangePasswordRequestDto;
import ru.example.ivtserver.exceptions.auth.InvalidDisposableToken;
import ru.example.ivtserver.exceptions.auth.NoUserException;

public interface ChangeParamUserService {

    void sendRecoverPassEmail(String email) throws NoUserException;

    void recoverPassword(String token, String newPassword) throws InvalidDisposableToken, NoUserException;

    void sendChangeEmail(ChangeEmailRequestDto dto, String email) throws NoUserException;

    void changeEmail(String token) throws InvalidDisposableToken, NoUserException;

    void changePassword(ChangePasswordRequestDto dto, String email) throws NoUserException;

}
