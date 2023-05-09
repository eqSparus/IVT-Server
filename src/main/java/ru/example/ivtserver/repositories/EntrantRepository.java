package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Entrant;

import java.util.UUID;

/**
 * Spring Data репозиторий для документов {@link Entrant} в базе данных Couchbase.
 */
public interface EntrantRepository extends CouchbaseRepository<Entrant, UUID> {
}
