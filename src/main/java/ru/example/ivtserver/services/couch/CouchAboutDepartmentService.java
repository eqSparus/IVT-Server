package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.dto.AboutDepartmentDto;
import ru.example.ivtserver.entities.request.AboutDepartmentRequest;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.AboutDepartmentRepository;
import ru.example.ivtserver.services.AboutDepartmentService;

import java.util.List;
import java.util.UUID;

/**
 * Реализация интерфейса {@link AboutDepartmentService} для работы с информацией "о кафедре" используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@RequiredArgsConstructor
public class CouchAboutDepartmentService implements AboutDepartmentService {

    AboutDepartmentRepository aboutDepartmentRepository;

    /**
     * Обновляет описание кафедры по заданному DTO {@link AboutDepartmentRequest}
     *
     * @param request DTO-объект, содержащий данные для обновления
     * @param id идентификатор информации
     * @return Обновленный объект {@link AboutDepartmentDto}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект.
     */
    @Override
    public AboutDepartmentDto updateAbout(AboutDepartmentRequest request, UUID id) throws NoIdException {
        return aboutDepartmentRepository.findById(id)
                .map(a -> {
                    a.setTitle(request.getTitle());
                    a.setDescription(request.getDescription());
                    return a;
                })
                .map(AboutDepartmentDto::of)
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));
    }

    /**
     * Получает список всех объектов {@link AboutDepartment}
     *
     * @return Список объектов {@link AboutDepartmentDto}
     */
    @Override
    public List<AboutDepartmentDto> getAll() {
        return aboutDepartmentRepository.findAll().stream()
                .map(AboutDepartmentDto::of)
                .toList();
    }
}
