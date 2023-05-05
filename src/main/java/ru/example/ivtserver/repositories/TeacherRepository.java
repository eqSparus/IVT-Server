package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.example.ivtserver.entities.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherRepository extends CouchbaseRepository<Teacher, UUID> {

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} ORDER BY position DESC LIMIT 1")
    Optional<Teacher> findTeacherLastPosition();

    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} ORDER BY position offset #{#skip} limit #{#size}")
    List<Teacher> findAllByOrderByPosition(@Param("skip") int skip, @Param("size") int size);
}
