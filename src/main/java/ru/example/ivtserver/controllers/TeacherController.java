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
import ru.example.ivtserver.utils.image.FileType;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Контролер для создания, обновления, удаления, получения всех преподавателя, обновления фотографии и получения её.
 */
@CrossOrigin(origins = "http://localhost:8081", methods = {
        RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT
})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/teacher")
@Validated
@Log4j2
public class TeacherController {

    TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * Конечная точка для создания объект {@link Teacher} на основе переданных данных.
     * Принимает объект типа {@link TeacherRequestDto} и объект типа {@link MultipartFile} с изображением в теле запроса.
     * Возвращает объект типа {@link Teacher}.
     * @param dto Объект типа {@link TeacherRequestDto} для создания {@link Teacher}.
     * @param img Объект типа {@link MultipartFile}, содержащий изображение преподавателя.
     * @return Объект типа {@link Teacher}, созданный на основе переданных данных.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Teacher createTeacher(
            @RequestPart(name = "data") @Valid TeacherRequestDto dto,
            @RequestPart(name = "img") @Valid @FileType MultipartFile img
    ) throws IOException {
        return teacherService.addTeacher(dto, img);
    }

    /**
     * Конечная точка для обновления объекта типа {@link Teacher} на основе переданного запроса.
     * Принимает объект типа {@link TeacherRequestDto} в формате JSON в теле запроса.
     * Возвращает объект типа {@link Teacher}.
     * @param dto Объект типа {@link TeacherRequestDto}, содержащий данные для обновления {@link Teacher}.
     * @return Обновленный объект типа {@link Teacher}.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Teacher updateTeacher(
            @RequestBody @Valid TeacherRequestDto dto
    ) {
        return teacherService.updateTeacher(dto);
    }

    /**
     * Конечная точка для обновления изображения преподавателя на основе переданного идентификатора.
     * Принимает объект типа {@link MultipartFile} с изображением в теле запроса и параметр {@code id}.
     * Возвращает объект типа {@link Map}.
     * @param file Объект типа {@link MultipartFile}, содержащий новое изображение преподавателя.
     * @param id Параметр типа {@link UUID}, содержащий идентификатор преподавателя.
     * @return Объект типа {@link Map} с единственной парой ключ/значение "url"/url обновленного изображения.
     */
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public Map<String, String> updateImgTeacher(
            @RequestPart(name = "img") @Valid @FileType MultipartFile file,
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        var url = teacherService.updateImg(file, id);
        return Map.of("url", url);
    }

    /**
     * Конечная точка для обновления позиции преподавателя на основе переданного идентификатора и позиции.
     * Принимает параметры {@code id} и {@code position} в виде целого числа.
     * Возвращает объект типа {@link Map} с единственной парой ключ/значение "position"/новой позицией преподавателя.
     * @param id Параметр типа {@link UUID}, содержащий идентификатор преподавателя.
     * @param position Параметр типа int, содержащий новую позицию преподавателя.
     * @return Объект типа {@link Map} с единственной парой ключ/значение "position"/новой позицией преподавателя.
     */
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"id", "position"})
    public Map<String, Integer> updatePositionTeacher(
            @RequestParam(name = "id") UUID id,
            @RequestParam(name = "position") int position
    ) {
        var newPosition = teacherService.updatePosition(position, id);
        return Map.of("position", newPosition);
    }

    /**
     * Удаляет преподавателя с указанным {@code id}.
     * @param id идентификатор удаляемого преподавателя
     * @return {@link ResponseEntity} с сообщением.
     */
    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> deleteTeacher(
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        teacherService.removeTeacher(id);
        return ResponseEntity.ok("Удаление преподавателя");
    }

    /**
     * Возвращает файл изображения для указанного преподавателя по имени файла {@code filename}.
     * @param filename имя файла изображения преподавателя
     * @return массив байтов, содержащий данные изображения в формате JPG
     */
    @GetMapping(path = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getTeacherImg(
            @PathVariable(name = "filename") String filename
    ) throws IOException {
        return teacherService.getImageTeacher(filename).getInputStream().readAllBytes();
    }

    /**
     * Возвращает список преподавателей с возможностью пропустить некоторое количество
     * преподавателей и ограничить размер ответа.
     * @param skip количество пропущенных преподавателей в начале списка
     * @param size максимальное количество возвращаемых преподавателей; -1, чтобы вернуть все результаты (по умолчанию)
     * @return список преподавателей, в количестве не превышающем размер, если он указан
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Teacher> getTeachers(
            @RequestParam(name = "skip", defaultValue = "0") int skip,
            @RequestParam(name = "size", defaultValue = "-1") int size
    ) {
        if (size == -1) {
            return teacherService.getTeachers(skip);
        }
        return teacherService.getTeachers(skip, size);
    }
}
