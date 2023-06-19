package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Department;
import ru.example.ivtserver.entities.mapper.request.DepartmentRequestDto;
import ru.example.ivtserver.repositories.DepartmentRepository;
import ru.example.ivtserver.services.DepartmentService;

/**
 * Реализация интерфейса {@link DepartmentService} для работы с описанием кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CouchDepartmentService implements DepartmentService {

    DepartmentRepository departmentRepository;

    @Autowired
    public CouchDepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * Обновляет описание кафедры по заданному DTO {@link DepartmentRequestDto}
     *
     * @param dto DTO-объект, содержащий данные для обновления
     * @return Обновленный объект {@link Department}
     */
    @Override
    public Department updateDepartment(DepartmentRequestDto dto) {
        var department = departmentRepository.findAll().get(0);

        department.setTitle(dto.getTitle());
        department.setSlogan(dto.getSlogan());
        department.setPhone(dto.getPhone());
        department.setEmail(dto.getEmail());
        department.setAddress(dto.getAddress());

        return departmentRepository.save(department);
    }

    /**
     * Возвращает описание кафедры {@link Department}
     *
     * @return Объект {@link Department}
     */
    @Override
    public Department getDepartment() {
        return departmentRepository.findAll().get(0);
    }

}
