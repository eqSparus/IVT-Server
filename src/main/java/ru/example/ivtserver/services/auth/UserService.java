package ru.example.ivtserver.services.auth;

import ru.example.ivtserver.entities.mapper.auth.AuthenticationToken;
import ru.example.ivtserver.entities.mapper.auth.request.UserRequestDto;
import ru.example.ivtserver.exceptions.auth.*;

/**
 * Интерфейс для работы с учетными данными пользователя.
 */
public interface UserService {

    /**
     * Выполняет процедуру аутентификации пользователя с использованием переданных учетных данных.
     * @param userDao Объект {@link UserRequestDto}, содержащий данные пользователя, который пытается пройти аутентификацию.
     * @return Токены аутентификации представленные {@link AuthenticationToken}.
     * @throws IncorrectCredentialsException бросается если переданные учетные данные неверны или не найдены.
     * @throws NoUserException бросается если пользователь не был найден.
     */
    AuthenticationToken login(UserRequestDto userDao) throws IncorrectCredentialsException, NoUserException;

    /**
     * Обновляет токен доступа и обновления, используя переданный токен обновления.
     * @param refreshToken Токен обновления, который будет использоваться для обновления токенов.
     * @return Новые токены аутентификации, представленные объектом {@link AuthenticationToken}.
     * @throws InvalidRefreshTokenException Если переданный токен обновления недействителен.
     * @throws NotExistsRefreshTokenException Если токен обновления не найден в базе данных.
     * @throws NoUserWithRefreshTokenException Если пользователь с заданным токеном обновления не найден в базе данных.
     */
    AuthenticationToken refreshToken(String refreshToken)
            throws InvalidRefreshTokenException, NotExistsRefreshTokenException, NoUserWithRefreshTokenException;

    /**
     * Осуществляет процедуру выхода из системы для пользователя с указанным токеном обновления.
     * @param refreshToken Токен обновления, который пытается выйти из системы.
     * @throws InvalidRefreshTokenException Если переданный токен обновления недействителен.
     */
    void logout(String refreshToken) throws InvalidRefreshTokenException;

}
