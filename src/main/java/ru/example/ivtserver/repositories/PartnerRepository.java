package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Partner;

import java.util.UUID;

public interface PartnerRepository extends CouchbaseRepository<Partner, UUID> {
}
