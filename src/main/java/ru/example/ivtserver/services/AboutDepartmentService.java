package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.dto.AboutDepartmentDto;
import ru.example.ivtserver.entities.request.AboutDepartmentRequest;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс сервиса для работы с информацией "о кафедре"
 */
public interface AboutDepartmentService {

    /**
     * Обновляет описание кафедры по заданному DTO {@link AboutDepartmentRequest}
     * @param request DTO-объект, содержащий данные для обновления
     * @param id идентификатор информации
     * @return Обновленный объект {@link AboutDepartmentDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект.
     */
    AboutDepartmentDto updateAbout(AboutDepartmentRequest request, UUID id) throws NoIdException;

    /**
     * Получает список всех объектов {@link AboutDepartmentDto}
     * @return Список объектов {@link AboutDepartmentDto}
     */
    List<AboutDepartmentDto> getAll();

}
