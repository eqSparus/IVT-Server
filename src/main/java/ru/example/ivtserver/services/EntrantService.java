package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.Entrant;
import ru.example.ivtserver.entities.mapper.request.EntrantRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с информацией абитуриентам
 */
public interface EntrantService {

    /**
     * Создает новою информацию абитуриенту по заданному DTO {@link EntrantRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для создания абитуриента
     * @return Созданная информация абитуриенту {@link Entrant}
     */
    Entrant create(EntrantRequestDto dto);

    /**
     * Обновляет информацию абитуриенту по заданному DTO {@link EntrantRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для обновления
     * @return Обновленная информация {@link Entrant}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному ID
     */
    Entrant update(EntrantRequestDto dto) throws NoIdException;

    /**
     * Удаляет информацию абитуриенту по заданному {@code id}
     *
     * @param id информации, которую нужно удалить
     */
    void delete(UUID id);

    /**
     * Получает список всей информации абитуриенту
     *
     * @return Список {@link List} абитуриентов {@link Entrant}
     */
    List<Entrant> getAll();

}
