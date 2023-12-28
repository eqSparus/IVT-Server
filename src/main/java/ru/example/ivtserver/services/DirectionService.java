package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.dto.DirectionDto;
import ru.example.ivtserver.entities.request.DirectionRequest;
import ru.example.ivtserver.exceptions.DirectionQuantityLimitException;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для сервиса для работы с направлениями кафедры
 */
public interface DirectionService {

    /**
     * Создает новое направление на кафедре по заданному DTO {@link DirectionRequest}
     *
     * @param request DTO-объект, содержащий данные для создания направления
     * @return Созданное направление {@link DirectionDto}
     * @throws DirectionQuantityLimitException выбрасывается если количество направлений превышает лимит
     */
    DirectionDto create(DirectionRequest request) throws DirectionQuantityLimitException;

    /**
     * Получает список всех направлений
     *
     * @return Список {@link List} направлений {@link DirectionDto}
     */
    List<DirectionDto> getAll();

    /**
     * Обновляет направление по заданному DTO {@link DirectionRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления
     * @param id идентификатор направления
     * @return Обновленное направление {@link DirectionDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному {@code ID}
     */
    DirectionDto update(DirectionRequest request, UUID id) throws NoIdException;

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
     * @return Список {@link List} направлений {@link DirectionDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному id
     */
    List<DirectionDto> swapPosition(UUID firstId, UUID lastId) throws NoIdException;

}
