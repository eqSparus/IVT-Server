package ru.example.ivtserver.services;

import org.springframework.lang.NonNull;
import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.dto.AboutDepartmentRequestDto;

import java.util.List;
import java.util.UUID;

public interface AboutDepartmentService {

    @NonNull
    AboutDepartment updateAbout(@NonNull AboutDepartmentRequestDto dto, @NonNull UUID id);

    List<AboutDepartment> getAll();

}
