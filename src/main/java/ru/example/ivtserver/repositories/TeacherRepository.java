package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import ru.example.ivtserver.entities.Teacher;

import java.util.UUID;

public interface TeacherRepository extends CouchbaseRepository<Teacher, UUID> {

}
