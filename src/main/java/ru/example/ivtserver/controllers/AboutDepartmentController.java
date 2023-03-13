package ru.example.ivtserver.controllers;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.dto.AboutDepartmentRequestDto;
import ru.example.ivtserver.services.AboutDepartmentService;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/about")
public class AboutDepartmentController {

    AboutDepartmentService aboutDepartmentService;


    @Autowired
    public AboutDepartmentController(AboutDepartmentService aboutDepartmentService) {
        this.aboutDepartmentService = aboutDepartmentService;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public AboutDepartment update(
            @RequestBody @Valid AboutDepartmentRequestDto dto,
            @RequestParam(name = "id") UUID id
    ) {
        return aboutDepartmentService.updateAbout(dto, id);
    }

}
