package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.dto.AboutDepartmentRequestDto;

import java.util.List;
import java.util.UUID;

public interface AboutDepartmentService {

    AboutDepartment updateAbout(AboutDepartmentRequestDto dto, UUID id);

    List<AboutDepartment> getAll();

}
