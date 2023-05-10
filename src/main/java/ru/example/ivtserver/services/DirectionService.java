package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.mapper.request.DirectionRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для сервиса для работы с направлениями кафедры
 */
public interface DirectionService {

    /**
     * Создает новое направление на кафедре по заданному DTO {@link DirectionRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для создания направления
     * @return Созданное направление {@link Direction}
     */
    Direction create(DirectionRequestDto dto);

    /**
     * Получает список всех направлений
     *
     * @return Список {@link List} направлений {@link Direction}
     */
    List<Direction> getAll();

    /**
     * Обновляет направление по заданному DTO {@link DirectionRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для обновления
     * @return Обновленное направление {@link Direction}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному {@code ID}
     */
    Direction update(DirectionRequestDto dto) throws NoIdException;

    /**
     * Удаляет направление по заданному {@code id}
     *
     * @param id направления, которое нужно удалить
     */
    void delete(UUID id);

    /**
     * Меняет позицию двух направлений местами
     *
     * @param firstId id первого направления
     * @param lastId  id второго направления
     * @return Список {@link List} направлений {@link Direction}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    List<Direction> swapPosition(UUID firstId, UUID lastId) throws NoIdException;

}
