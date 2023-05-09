package ru.example.ivtserver.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.User;

import java.util.Optional;
import java.util.UUID;


/**
 * Spring Data репозиторий для документов {@link User} в базе данных Couchbase.
 */
public interface UserRepository extends CouchbaseRepository<User, UUID> {

    /**
     * Находит и возвращает объект {@link Optional} с пользователем, соответствующим указанному электронному адресу.
     * Если такой пользователь не найден, метод возвращает пустой {@link Optional}.
     * @param email электронный адрес пользователя для поиска
     * @return {@link Optional} с объектом пользователя, соответствующим указанному e-mail, или пустой {@link Optional#empty()}, если такой пользователь не найден
     */
    Optional<User> findByEmail(@NotNull String email);

}
