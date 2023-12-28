package ru.example.ivtserver.services.auth;

import ru.example.ivtserver.entities.dto.auth.AuthenticationTokenDto;
import ru.example.ivtserver.entities.request.auth.UserRequest;
import ru.example.ivtserver.exceptions.auth.*;

/**
 * Интерфейс для работы с учетными данными пользователя.
 */
public interface UserService {

    /**
     * Выполняет процедуру аутентификации пользователя с использованием переданных учетных данных.
     * @param userDao Объект {@link UserRequest}, содержащий данные пользователя, который пытается пройти аутентификацию.
     * @return Токены аутентификации представленные {@link AuthenticationTokenDto}.
     * @throws IncorrectCredentialsException бросается если переданные учетные данные неверны или не найдены.
     * @throws NoUserException бросается если пользователь не был найден.
     */
    AuthenticationTokenDto login(UserRequest userDao) throws IncorrectCredentialsException, NoUserException;

    /**
     * Обновляет токен доступа и обновления, используя переданный токен обновления.
     * @param refreshToken Токен обновления, который будет использоваться для обновления токенов.
     * @return Новые токены аутентификации, представленные объектом {@link AuthenticationTokenDto}.
     * @throws NotExistsRefreshTokenException Если токен обновления не найден в базе данных.
     * @throws NoUserWithRefreshTokenException Если пользователь с заданным токеном обновления не найден в базе данных.
     * @throws ExpiredExpirationRefreshTokenException Если срок жизни токена обновления истек.
     */
    AuthenticationTokenDto refreshToken(String refreshToken)
            throws NotExistsRefreshTokenException, NoUserWithRefreshTokenException, ExpiredExpirationRefreshTokenException;

    /**
     * Осуществляет процедуру выхода из системы для пользователя с указанным токеном обновления.
     * @param refreshToken Токен обновления, который пытается выйти из системы.
     * @throws NotExistsRefreshTokenException Если переданный токен обновления не существует.
     */
    void logout(String refreshToken) throws NotExistsRefreshTokenException;

}
