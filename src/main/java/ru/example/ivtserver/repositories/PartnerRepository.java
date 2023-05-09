package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Partner;

import java.util.UUID;

/**
 * Spring Data репозиторий для документов {@link Partner} в базе данных Couchbase.
 */
public interface PartnerRepository extends CouchbaseRepository<Partner, UUID> {
}
