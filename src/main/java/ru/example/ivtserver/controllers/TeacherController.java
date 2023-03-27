package ru.example.ivtserver.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.dto.TeacherRequestDto;
import ru.example.ivtserver.services.TeacherService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/teacher")
public class TeacherController {

    TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Teacher createTeacher(
            @RequestPart(name = "data") TeacherRequestDto dto,
            @RequestPart(name = "img") MultipartFile img
    ) throws IOException {
        return teacherService.addTeacher(dto, img);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Teacher updateTeacher(
            @RequestBody TeacherRequestDto dto
    ) {
        return teacherService.updateTeacher(dto);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public Map<String, String> updateImgTeacher(
            @RequestPart(name = "img") MultipartFile file,
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        var url = teacherService.updateImg(file, id);
        return Map.of("url", url);
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> deleteTeacher(
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        teacherService.removeTeacher(id);
        return ResponseEntity.ok("Удаление преподавателя");
    }
}
