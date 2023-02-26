package ru.example.ivtserver.services.auth;

import org.springframework.lang.NonNull;
import ru.example.ivtserver.entities.dto.auth.*;
import ru.example.ivtserver.exceptions.auth.*;

public interface UserService {

    @NonNull
    AuthenticationDto login(@NonNull UserRequestDto userDao) throws IncorrectCredentialsException;

    @NonNull
    AuthenticationDto refreshToken(@NonNull RefreshTokenDto refreshDto)
            throws InvalidRefreshTokenException, NotExistsRefreshTokenException, NoUserWithRefreshTokenException;

    void logout(@NonNull RefreshTokenDto dto) throws InvalidRefreshTokenException;

    void sendRecoverPassEmail(@NonNull String email) throws NoUserException;

    void recoverPassword(@NonNull String token, @NonNull String newPassword)
            throws InvalidDisposableToken, NoUserException;

    void sendChangeEmail(@NonNull ChangeEmailDto dto, @NonNull String email) throws NoUserException;

    void changeEmail(@NonNull String token) throws InvalidDisposableToken, NoUserException;

    void changePassword(@NonNull ChangePasswordDto dto, @NonNull String email) throws NoUserException;

}
