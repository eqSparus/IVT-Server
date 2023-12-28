package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.dto.EntrantDto;
import ru.example.ivtserver.entities.request.EntrantRequest;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с информацией абитуриентам
 */
public interface EntrantService {

    /**
     * Создает новою информацию абитуриенту по заданному DTO {@link EntrantRequest}
     *
     * @param request DTO-объект, содержащий данные для создания абитуриента
     * @return Созданная информация абитуриенту {@link EntrantDto}
     */
    EntrantDto create(EntrantRequest request);

    /**
     * Обновляет информацию абитуриенту по заданному DTO {@link EntrantRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления
     * @param id идентификатор информации
     * @return Обновленная информация {@link EntrantDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект по заданному ID
     */
    EntrantDto update(EntrantRequest request, UUID id) throws NoIdException;

    /**
     * Удаляет информацию абитуриенту по заданному {@code id}
     *
     * @param id информации, которую нужно удалить
     */
    void delete(UUID id);

    /**
     * Получает список всей информации абитуриенту
     *
     * @return Список {@link List} абитуриентов {@link EntrantDto}
     */
    List<EntrantDto> getAll();

}
