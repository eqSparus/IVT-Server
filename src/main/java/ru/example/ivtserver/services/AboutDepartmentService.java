package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.mapper.request.AboutDepartmentRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.util.List;

public interface AboutDepartmentService {

    AboutDepartment updateAbout(AboutDepartmentRequestDto dto) throws NoIdException;

    List<AboutDepartment> getAll();

}
