package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import ru.example.ivtserver.entities.Direction;

import java.util.Optional;
import java.util.UUID;

public interface DirectionRepository extends CouchbaseRepository<Direction, UUID> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} ORDER BY position DESC LIMIT 1")
    Optional<Direction> findDirectionLastPosition();

}
