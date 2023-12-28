package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Department;
import ru.example.ivtserver.entities.dto.DepartmentDto;
import ru.example.ivtserver.entities.request.DepartmentRequest;
import ru.example.ivtserver.repositories.DepartmentRepository;
import ru.example.ivtserver.services.DepartmentService;

/**
 * Реализация интерфейса {@link DepartmentService} для работы с описанием кафедры используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class CouchDepartmentService implements DepartmentService {

    DepartmentRepository departmentRepository;

    /**
     * Обновляет описание кафедры по заданному DTO {@link DepartmentRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления
     * @return Обновленный объект {@link DepartmentDto}
     */
    @Override
    public DepartmentDto updateDepartment(DepartmentRequest request) {
        return departmentRepository.findFirstBy()
                .map(d -> {
                    d.setTitle(request.getTitle());
                    d.setSlogan(request.getSlogan());
                    d.setPhone(request.getPhone());
                    d.setEmail(request.getEmail());
                    d.setAddress(request.getAddress());
                    return d;
                })
                .map(departmentRepository::save)
                .map(DepartmentDto::of).orElseThrow();
    }

    /**
     * Возвращает описание кафедры {@link Department}
     *
     * @return Объект {@link DepartmentDto}
     */
    @Override
    public DepartmentDto getDepartment() {
        return departmentRepository.findFirstBy()
                .map(DepartmentDto::of)
                .orElseThrow();
    }

}
