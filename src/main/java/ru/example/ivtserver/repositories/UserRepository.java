package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.example.ivtserver.entities.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CouchbaseRepository<User, UUID> {

    Optional<User> findByEmail(@NonNull String email);

}
