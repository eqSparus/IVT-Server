package ru.example.ivtserver.services;

import org.springframework.lang.NonNull;
import ru.example.ivtserver.entities.Department;
import ru.example.ivtserver.entities.dto.DepartmentRequestDto;

public interface DepartmentService {

    @NonNull
    Department updateDepartment(@NonNull DepartmentRequestDto dto);

    @NonNull
    Department getDepartment();

}
