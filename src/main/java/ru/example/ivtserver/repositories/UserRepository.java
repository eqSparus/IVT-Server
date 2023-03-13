package ru.example.ivtserver.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;
import ru.example.ivtserver.entities.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CouchbaseRepository<User, UUID> {

    Optional<User> findByEmail(@NotNull String email);

}
