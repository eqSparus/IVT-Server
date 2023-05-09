package ru.example.ivtserver.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.RefreshToken;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data репозиторий для документов {@link RefreshToken} в базе данных Couchbase.
 */
public interface RefreshTokenRepository extends CouchbaseRepository<RefreshToken, UUID> {

    /**
     * Ищет токен обновления с указанным значением token.
     * @param token значение токена обновления для поиска
     * @return {@link Optional} с найденным токеном обновления или пустое {@link Optional#empty()}, если токен не найден
     */
    Optional<RefreshToken> findByToken(@NotNull String token);

    /**
     * Удаляет токен обновления с указанным значением token.
     * @param token значение токена обновления, который необходимо удалить
     */
    void deleteByToken(@NotNull String token);

    /**
     * Удаляет все токены обновления для пользователя с указанным идентификатором.
     * @param userId идентификатор пользователя для удаления токенов обновления
     */
    void deleteAllByUserId(@NotNull String userId);

}
