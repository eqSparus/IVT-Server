package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.mapper.request.TeacherRequestDto;
import ru.example.ivtserver.services.TeacherService;
import ru.example.ivtserver.utils.image.ImgType;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/teacher")
@Validated
@Log4j2
public class TeacherController {

    TeacherService teacherService;

    public static final String KEY = ".jpg";

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Teacher createTeacher(
            @RequestPart(name = "data") @Valid TeacherRequestDto dto,
            @RequestPart(name = "img") @Valid @ImgType MultipartFile img
    ) throws IOException {
        return teacherService.addTeacher(dto, img);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Teacher updateTeacher(
            @RequestBody @Valid TeacherRequestDto dto
    ) {
        return teacherService.updateTeacher(dto);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public Map<String, String> updateImgTeacher(
            @RequestPart(name = "img") @Valid @ImgType MultipartFile file,
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        var url = teacherService.updateImg(file, id);
        return Map.of("url", url);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"id", "position"})
    public Map<String, Integer> updatePositionTeacher(
            @RequestParam(name = "id") UUID id,
            @RequestParam(name = "position") int position
    ) {
        var newPosition = teacherService.updatePosition(position, id);
        return Map.of("position", newPosition);
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> deleteTeacher(
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        teacherService.removeTeacher(id);
        return ResponseEntity.ok("Удаление преподавателя");
    }

    @GetMapping(path = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getTeacherImg(
            @PathVariable(name = "filename") String filename
    ) throws IOException {
        log.info("1111");
        return teacherService.getImageTeacher(filename).getInputStream().readAllBytes();
    }
}
