package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.dto.DepartmentDto;
import ru.example.ivtserver.entities.request.DepartmentRequest;

/**
 * Интерфейс для сервиса для работы с описанием кафедры
 */
public interface DepartmentService {

    /**
     * Обновляет описание кафедры по заданному DTO {@link DepartmentRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления
     * @return Обновленный объект {@link DepartmentDto}
     */
    DepartmentDto updateDepartment(DepartmentRequest request);

    /**
     * Возвращает описание кафедры {@link DepartmentDto}
     *
     * @return Объект {@link DepartmentDto}
     */
    DepartmentDto getDepartment();

}
