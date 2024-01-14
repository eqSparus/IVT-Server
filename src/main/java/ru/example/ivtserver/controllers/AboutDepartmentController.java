package ru.example.ivtserver.controllers;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.dto.AboutDepartmentDto;
import ru.example.ivtserver.entities.request.AboutDepartmentRequest;
import ru.example.ivtserver.services.AboutDepartmentService;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/abouts")
@RequiredArgsConstructor
public class AboutDepartmentController {

    AboutDepartmentService aboutDepartmentService;

    /**
     * Контрольная точка для обновления информации о кафедре и возврата обновленного объекта {@link AboutDepartmentDto}.
     *
     * @param request Объект {@link AboutDepartmentRequest}, содержащий информацию о кафедре для обновления.
     * @param id Объект {@link UUID}, идентификатор информации о кафедре.
     * @return Объект {@link AboutDepartmentDto}, представляющий обновленную информацию о кафедре.
     */
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public AboutDepartmentDto update(
            @RequestBody @Valid AboutDepartmentRequest request,
            @PathVariable(name = "id") UUID id
    ) {
        return aboutDepartmentService.updateAbout(request, id);
    }

}
