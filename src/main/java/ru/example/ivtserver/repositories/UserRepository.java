package ru.example.ivtserver.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.User;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends CouchbaseRepository<User, UUID> {

    Optional<User> findByEmail(@NotNull String email);

}
