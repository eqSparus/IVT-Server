package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.example.ivtserver.entities.Department;
import ru.example.ivtserver.entities.dto.DepartmentRequestDto;
import ru.example.ivtserver.repositories.DepartmentRepository;
import ru.example.ivtserver.services.DepartmentService;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Log4j2
public class CouchDepartmentService implements DepartmentService {

    DepartmentRepository departmentRepository;

    @Autowired
    public CouchDepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @NonNull
    @Override
    public Department updateDepartment(@NonNull DepartmentRequestDto dto) {
        var department = departmentRepository.findAll().get(0);
        department.setTitle(dto.getTitle());
        department.setSlogan(dto.getSlogan());
        department.setPhone(dto.getPhone());
        department.setEmail(dto.getEmail());
        department.setAddress(dto.getAddress());

        departmentRepository.save(department);
        return department;
    }

    @NonNull
    @Override
    public Department getDepartment() {
        return departmentRepository.findAll().get(0);
    }

}
