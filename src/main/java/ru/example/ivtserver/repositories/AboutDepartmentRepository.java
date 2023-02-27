package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;
import ru.example.ivtserver.entities.AboutDepartment;

import java.util.UUID;

@Repository
public interface AboutDepartmentRepository extends CouchbaseRepository<AboutDepartment, UUID> {
}
