package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Department;

import java.util.List;

public interface DepartmentRepository extends CouchbaseRepository<Department, String> {

    @Override
    List<Department> findAll();

}
