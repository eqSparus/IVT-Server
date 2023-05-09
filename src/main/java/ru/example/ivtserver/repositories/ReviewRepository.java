package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Review;

import java.util.UUID;

/**
 * Spring Data репозиторий для документов {@link Review} в базе данных Couchbase.
 */
public interface ReviewRepository extends CouchbaseRepository<Review, UUID> {
}
