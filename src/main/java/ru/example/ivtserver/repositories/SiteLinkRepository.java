package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;
import ru.example.ivtserver.entities.SiteLink;

import java.util.UUID;

@Repository
public interface SiteLinkRepository extends CouchbaseRepository<SiteLink, UUID> {
}
