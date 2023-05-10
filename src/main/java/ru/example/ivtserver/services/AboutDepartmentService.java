package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.mapper.request.AboutDepartmentRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;

/**
 * Интерфейс сервиса для работы с информацией "о кафедре"
 */
public interface AboutDepartmentService {

    /**
     * Обновляет описание кафедры по заданному DTO {@link AboutDepartmentRequestDto}
     * @param dto DTO-объект, содержащий данные для обновления
     * @return Обновленный объект {@link AboutDepartment}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект.
     */
    AboutDepartment updateAbout(AboutDepartmentRequestDto dto) throws NoIdException;

    /**
     * Получает список всех объектов {@link AboutDepartment}
     * @return Список объектов {@link AboutDepartment}
     */
    List<AboutDepartment> getAll();

}
