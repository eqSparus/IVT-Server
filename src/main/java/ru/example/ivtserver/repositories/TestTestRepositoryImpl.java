package ru.example.ivtserver.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.example.ivtserver.entities.Test;

import java.util.Optional;

@Repository
public interface TestTestRepositoryImpl extends CrudRepository<Test, String> {

    @Override
    <S extends Test> S save(S entity);

    @Override
    Optional<Test> findById(String aLong);
}
