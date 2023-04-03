package ru.example.ivtserver.services.couch;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Override
    public Department updateDepartment(DepartmentRequestDto dto) {
        var department = departmentRepository.findAll().get(0);

        log.debug("Старая общая информации {}", department);

        department.setTitle(dto.getTitle());
        department.setSlogan(dto.getSlogan());
        department.setPhone(dto.getPhone());
        department.setEmail(dto.getEmail());
        department.setAddress(dto.getAddress());

        log.debug("Старая новая информации {}", department);

        return departmentRepository.save(department);
    }


    @Override
    public Department getDepartment() {
        return departmentRepository.findAll().get(0);
    }

}
