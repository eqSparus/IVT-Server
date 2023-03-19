package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Entrant;

import java.util.UUID;

public interface EntrantRepository extends CouchbaseRepository<Entrant, UUID> {
}
