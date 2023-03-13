package ru.example.ivtserver.services;

import ru.example.ivtserver.entities.Department;
import ru.example.ivtserver.entities.dto.DepartmentRequestDto;

public interface DepartmentService {


    Department updateDepartment( DepartmentRequestDto dto);


    Department getDepartment();

}
