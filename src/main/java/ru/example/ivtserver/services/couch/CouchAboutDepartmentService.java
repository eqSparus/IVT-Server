package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.mapper.request.AboutDepartmentRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.AboutDepartmentRepository;
import ru.example.ivtserver.services.AboutDepartmentService;

import java.util.List;

/**
 * Реализация интерфейса {@link AboutDepartmentService} для работы с информацией "о кафедре" используя базу данных Couchbase
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CouchAboutDepartmentService implements AboutDepartmentService {

    AboutDepartmentRepository aboutDepartmentRepository;

    @Autowired
    public CouchAboutDepartmentService(AboutDepartmentRepository aboutDepartmentRepository) {
        this.aboutDepartmentRepository = aboutDepartmentRepository;
    }


    /**
     * Обновляет описание кафедры по заданному DTO {@link AboutDepartmentRequestDto}
     * @param dto DTO-объект, содержащий данные для обновления
     * @return Обновленный объект {@link AboutDepartment}
     * @throws NoIdException Исключение, которое выбрасывается, если не найден объект.
     */
    @Override
    public AboutDepartment updateAbout(AboutDepartmentRequestDto dto) throws NoIdException {
        var aboutInfo = aboutDepartmentRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        aboutInfo.setTitle(dto.getTitle());
        aboutInfo.setDescription(dto.getDescription());

        return aboutDepartmentRepository.save(aboutInfo);
    }

    /**
     * Получает список всех объектов {@link AboutDepartment}
     * @return Список объектов {@link AboutDepartment}
     */
    @Override
    public List<AboutDepartment> getAll() {
        return aboutDepartmentRepository.findAll();
    }
}
