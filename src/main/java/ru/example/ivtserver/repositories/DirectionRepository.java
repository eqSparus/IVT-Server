package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Direction;

import java.util.UUID;

public interface DirectionRepository extends CouchbaseRepository<Direction, UUID> {
}
