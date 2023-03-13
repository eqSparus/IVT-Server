package ru.example.ivtserver.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;
import ru.example.ivtserver.entities.RefreshToken;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends CouchbaseRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(@NotNull String token);

    void deleteByToken(@NotNull String token);

    void deleteAllByUserId(@NotNull String userId);

}
