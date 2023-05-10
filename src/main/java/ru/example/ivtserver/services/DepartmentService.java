package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.Department;
import ru.example.ivtserver.entities.mapper.request.DepartmentRequestDto;

/**
 * Интерфейс для сервиса для работы с описанием кафедры
 */
public interface DepartmentService {

    /**
     * Обновляет описание кафедры по заданному DTO {@link DepartmentRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для обновления
     * @return Обновленный объект {@link Department}
     */
    Department updateDepartment(DepartmentRequestDto dto);

    /**
     * Возвращает описание кафедры {@link Department}
     *
     * @return Объект {@link Department}
     */
    Department getDepartment();

}
