package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import ru.example.ivtserver.entities.Direction;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data репозиторий для документов {@link Direction} в базе данных Couchbase.
 */
public interface DirectionRepository extends CouchbaseRepository<Direction, UUID> {

    /**
     * Находит направление с наивысшей позицией в порядке сортировки (по убыванию).
     * Если в базе данных нет направлений, метод вернет пустое {@link Optional}.
     * @return {@link Optional} с направлением с наивысшей позицией или пустое {@link Optional#empty()}, если такого направления нет
     */
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} ORDER BY position DESC LIMIT 1")
    Optional<Direction> findDirectionLastPosition();

}
