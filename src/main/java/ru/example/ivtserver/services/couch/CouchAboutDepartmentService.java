package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.dto.AboutDepartmentRequestDto;
import ru.example.ivtserver.repositories.AboutDepartmentRepository;
import ru.example.ivtserver.services.AboutDepartmentService;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CouchAboutDepartmentService implements AboutDepartmentService {

    AboutDepartmentRepository aboutDepartmentRepository;

    @Autowired
    public CouchAboutDepartmentService(AboutDepartmentRepository aboutDepartmentRepository) {
        this.aboutDepartmentRepository = aboutDepartmentRepository;
    }

    @NonNull
    @Override
    public AboutDepartment updateAbout(@NonNull AboutDepartmentRequestDto dto, @NonNull UUID id) {

        var aboutInfo = aboutDepartmentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        aboutInfo.setTitle(dto.getTitle());
        aboutInfo.setDescription(dto.getDescription());
        aboutDepartmentRepository.save(aboutInfo);

        return aboutInfo;
    }

    @Override
    public List<AboutDepartment> getAll() {
        return aboutDepartmentRepository.findAll();
    }
}
