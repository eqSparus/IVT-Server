package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.SiteLink;

import java.util.UUID;


public interface SiteLinkRepository extends CouchbaseRepository<SiteLink, UUID> {
}
