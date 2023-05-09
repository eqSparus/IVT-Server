package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.SiteLink;

import java.util.UUID;

/**
 * Spring Data репозиторий для документов {@link SiteLink} в базе данных Couchbase.
 */
public interface SiteLinkRepository extends CouchbaseRepository<SiteLink, UUID> {
}
