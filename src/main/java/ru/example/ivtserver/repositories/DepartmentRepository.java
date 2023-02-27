package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;
import ru.example.ivtserver.entities.Department;

import java.util.List;

@Repository
public interface DepartmentRepository extends CouchbaseRepository<Department, String> {

    @Override
    List<Department> findAll();

}
