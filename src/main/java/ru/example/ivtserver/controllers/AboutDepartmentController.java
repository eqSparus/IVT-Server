package ru.example.ivtserver.controllers;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.mapper.request.AboutDepartmentRequestDto;
import ru.example.ivtserver.services.AboutDepartmentService;

/**
 * Контролер для обновления информации "о кафедре".
 */
@CrossOrigin(origins = "http://localhost:8081", methods = RequestMethod.PUT)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/about")
public class AboutDepartmentController {

    AboutDepartmentService aboutDepartmentService;

    @Autowired
    public AboutDepartmentController(AboutDepartmentService aboutDepartmentService) {
        this.aboutDepartmentService = aboutDepartmentService;
    }

    /**
     * Контрольная точка для обновления информации о кафедре и возврата обновленного объекта {@link AboutDepartment}.
     *
     * @param dto Объект {@link AboutDepartmentRequestDto}, содержащий информацию о кафедре для обновления.
     * @return Объект {@link AboutDepartment}, представляющий обновленную информацию о кафедре.
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public AboutDepartment update(
            @RequestBody @Valid AboutDepartmentRequestDto dto
    ) {
        return aboutDepartmentService.updateAbout(dto);
    }

}
