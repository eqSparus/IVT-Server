package ru.example.ivtserver.repositories;

import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.example.ivtserver.entities.Teacher;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Spring Data репозиторий для документов {@link Teacher} в базе данных Couchbase.
 */
public interface TeacherRepository extends CouchbaseRepository<Teacher, UUID> {

    /**
     * Находит преподавателя с наивысшей позицией в порядке сортировки (по убыванию).
     * Если в базе данных нет преподавателей, метод вернет пустое {@link Optional}.
     * @return {@link Optional} с преподавателем с наивысшей позицией или пустое {@link Optional#empty()}, если такого преподавателя нет
     */
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} ORDER BY position DESC LIMIT 1")
    Optional<Teacher> findTeacherLastPosition();

    /**
     * Ищет и возвращает список преподавателей, отсортированных по полю "position". Начинает выборку
     * с элемента, указанного аргументом {@code skip}, возвращает количество элементов, указанных в
     * аргументе {@code size}.
     * @param skip количество пропускаемых элементов (отступ) при выборке
     * @param size количество элементов (размер) результата выборки
     * @return список преподавателей, отсортированный по полю "position"
     */
    @Query("#{#n1ql.selectEntity} WHERE #{#n1ql.filter} ORDER BY position offset #{#skip} limit #{#size}")
    Stream<Teacher> findAllByOrderByPosition(@Param("skip") int skip, @Param("size") int size);
}
