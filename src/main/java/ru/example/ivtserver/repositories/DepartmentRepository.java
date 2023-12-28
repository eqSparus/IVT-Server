package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Department;

import java.util.Optional;

/**
 * Spring Data репозиторий для документов {@link Department} в базе данных Couchbase.
 */
public interface DepartmentRepository extends CouchbaseRepository<Department, String> {

    Optional<Department> findFirstBy();

}
