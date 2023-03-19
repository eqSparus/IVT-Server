package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.AboutDepartment;

import java.util.UUID;

public interface AboutDepartmentRepository extends CouchbaseRepository<AboutDepartment, UUID> {
}
