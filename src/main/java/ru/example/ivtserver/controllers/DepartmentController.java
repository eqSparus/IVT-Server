package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.dto.DepartmentDto;
import ru.example.ivtserver.entities.request.DepartmentRequest;
import ru.example.ivtserver.services.DepartmentService;

@CrossOrigin(origins = "http://localhost:8081", methods = RequestMethod.PUT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/department")
@RequiredArgsConstructor
public class DepartmentController {

    DepartmentService departmentService;

    /**
     * Конечная точка для обновляет информацию о кафедре и возврата обновленного объекта {@link DepartmentDto}.
     *
     * @param dto Объект {@link DepartmentRequest}, содержащий информацию о департаменте для обновления.
     * @return Объект {@link DepartmentDto}, представляющий обновленную информацию о департаменте.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DepartmentDto updateDepartment(
            @RequestBody @Valid DepartmentRequest dto
    ) {
        return departmentService.updateDepartment(dto);
    }

}
