package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.Department;
import ru.example.ivtserver.entities.mapper.request.DepartmentRequestDto;
import ru.example.ivtserver.services.DepartmentService;

/**
 * Контролер для обновления общей информации о кафедре.
 */
@CrossOrigin(origins = "http://localhost:8081", methods = RequestMethod.PUT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/department")
public class DepartmentController {

    DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Конечная точка для обновляет информацию о кафедре и возврата обновленного объекта {@link Department}.
     * @param dto Объект {@link DepartmentRequestDto}, содержащий информацию о департаменте для обновления.
     * @return Объект {@link Department}, представляющий обновленную информацию о департаменте.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Department updateDepartment(
            @RequestBody @Valid DepartmentRequestDto dto
    ) {
        return departmentService.updateDepartment(dto);
    }

}
