package ru.example.ivtserver.services.auth;

import ru.example.ivtserver.entities.mapper.auth.request.ChangeEmailRequestDto;
import ru.example.ivtserver.entities.mapper.auth.request.ChangePasswordRequestDto;
import ru.example.ivtserver.exceptions.auth.InvalidDisposableToken;
import ru.example.ivtserver.exceptions.auth.NoUserException;

/**
 * Интерфейс для изменения учетных данных пользователя.
 */
public interface ChangeParamUserService {

    /**
     * Отправляет электронное письмо для восстановления пароля для пользователя с заданным адресом электронной почты.
     * @param email Адрес электронной почты пользователя, который забыл свой пароль.
     * @throws NoUserException бросается если пользователь с указанным адресом электронной почты не найден в базе данных.
     */
    void sendRecoverPassEmail(String email) throws NoUserException;

    /**
     * Изменяет пароль для пользователя с использованием заданного токена восстановления.
     * @param token Токен, который будет использоваться для восстановления пароля пользователя.
     * @param newPassword Новый пароль, который будет установлен для пользователя.
     * @throws InvalidDisposableToken бросается если заданный токен восстановления недействительный или истек.
     * @throws NoUserException бросается если пользователь, для которого выполняется операция изменения пароля, не найден в базе данных.
     */
    void recoverPassword(String token, String newPassword) throws InvalidDisposableToken, NoUserException;


    /**
     * Отправляет электронное письмо для изменения адреса электронной почты для пользователя с заданным адресом электронной почты.
     * @param dto Запрос {@link ChangeEmailRequestDto} на изменение адреса электронной почты пользователя.
     * @param email Адрес электронной почты пользователя, который хочет изменить свой адрес электронной почты.
     * @throws NoUserException бросается если пользователь с указанным адресом электронной почты не найден в базе данных.
     */
    void sendChangeEmail(ChangeEmailRequestDto dto, String email) throws NoUserException;

    /**
     * Изменяет адрес электронной почты для пользователя с использованием заданного токена изменения адреса электронной почты.
     * @param token Токен, который будет использоваться для изменения адреса электронной почты пользователя.
     * @throws InvalidDisposableToken бросается если заданный токен недействителен или истек.
     * @throws NoUserException бросается если пользователь, для которого выполняется операция изменения адреса электронной почты, не найден в базе данных.
     */
    void changeEmail(String token) throws InvalidDisposableToken, NoUserException;

    /**
     * Изменяет пароль для пользователя с использованием данных из запроса на изменение пароля.
     * @param dto Запрос на изменение пароля пользователя {@link ChangePasswordRequestDto}.
     * @param email Адрес электронной почты пользователя, для которого выполняется операция изменения пароля.
     * @throws NoUserException бросается если пользователь, для которого выполняется операция изменения пароля, не найден в базе данных.
     */
    void changePassword(ChangePasswordRequestDto dto, String email) throws NoUserException;

    /**
     * Проверяет, действителен ли заданный токен для восстановления пароля.
     * @param token Токен для восстановления пароля, который нужно проверить на действительность.
     * @return {@code true}, если токен действителен; {@code false} в противном случае.
     */
    boolean isValidTokenPassword(String token);

}
