package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Review;

import java.util.UUID;

public interface ReviewRepository extends CouchbaseRepository<Review, UUID> {
}
