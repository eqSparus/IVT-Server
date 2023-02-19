package ru.example.ivtserver.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.example.ivtserver.entities.Test;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TestTestRepositoryImpl extends CrudRepository<Test, UUID> {

    @Override
    <S extends Test> S save(S entity);

    @Override
    Optional<Test> findById(UUID aLong);

    @Override
    Iterable<Test> findAll();

    @Override
    void deleteById(UUID uuid);
}
