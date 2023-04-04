package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.mapper.request.AboutDepartmentRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;
import ru.example.ivtserver.repositories.AboutDepartmentRepository;
import ru.example.ivtserver.services.AboutDepartmentService;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
@Service
public class CouchAboutDepartmentService implements AboutDepartmentService {

    AboutDepartmentRepository aboutDepartmentRepository;

    @Autowired
    public CouchAboutDepartmentService(AboutDepartmentRepository aboutDepartmentRepository) {
        this.aboutDepartmentRepository = aboutDepartmentRepository;
    }


    @Override
    public AboutDepartment updateAbout(AboutDepartmentRequestDto dto) throws NoIdException {
        var aboutInfo = aboutDepartmentRepository.findById(dto.getId())
                .orElseThrow(() -> new NoIdException("Идентификатор не найден"));

        log.debug("Идентификатор элемента информации {}", dto.getId());
        log.debug("Старая информации о кафедре {}", aboutInfo);

        aboutInfo.setTitle(dto.getTitle());
        aboutInfo.setDescription(dto.getDescription());

        log.debug("Новая информации о кафедре {}", aboutInfo);

        return aboutDepartmentRepository.save(aboutInfo);
    }

    @Override
    public List<AboutDepartment> getAll() {
        return aboutDepartmentRepository.findAll();
    }
}
