package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.dto.TeacherDto;
import ru.example.ivtserver.entities.request.TeacherRequest;
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
@RequestMapping(path = "/teachers")
@Validated
@RequiredArgsConstructor
public class TeacherController {

    TeacherService teacherService;

    /**
     * Конечная точка для создания объект {@link Teacher} на основе переданных данных.
     * Принимает объект типа {@link TeacherRequest} и объект типа {@link MultipartFile} с изображением в теле запроса.
     * Возвращает объект типа {@link TeacherDto}.
     *
     * @param request Объект типа {@link TeacherRequest} для создания {@link Teacher}.
     * @param img     Объект типа {@link MultipartFile}, содержащий изображение преподавателя.
     * @return Объект типа {@link TeacherDto}, созданный на основе переданных данных.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public TeacherDto createTeacher(
            @RequestPart(name = "data") @Valid TeacherRequest request,
            @RequestPart(name = "img") @Valid @FileType MultipartFile img
    ) {
        return teacherService.addTeacher(request, img);
    }

    /**
     * Конечная точка для обновления объекта типа {@link Teacher} на основе переданного запроса.
     * Принимает объект типа {@link TeacherRequest} в формате JSON в теле запроса.
     * Возвращает объект типа {@link TeacherDto}.
     *
     * @param request Объект типа {@link TeacherRequest}, содержащий данные для обновления {@link Teacher}.
     * @param id      идентификатор преподавателя.
     * @return Обновленный объект типа {@link TeacherDto}.
     */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TeacherDto updateTeacher(
            @RequestBody @Valid TeacherRequest request,
            @PathVariable(name = "id") UUID id
    ) {
        return teacherService.updateTeacher(request, id);
    }

    /**
     * Конечная точка для обновления изображения преподавателя на основе переданного идентификатора.
     * Принимает объект типа {@link MultipartFile} с изображением в теле запроса и параметр {@code id}.
     * Возвращает объект типа {@link Map}.
     *
     * @param file Объект типа {@link MultipartFile}, содержащий новое изображение преподавателя.
     * @param id   Параметр типа {@link UUID}, содержащий идентификатор преподавателя.
     * @return Объект типа {@link Map} с единственной парой ключ/значение "url"/url обновленного изображения.
     */
    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> updateImgTeacher(
            @RequestPart(name = "img") @Valid @FileType MultipartFile file,
            @PathVariable(name = "id") UUID id
    ) {
        var url = teacherService.updateImg(file, id);
        return Map.of("url", url);
    }

    /**
     * Конечная точка для обновления позиции преподавателя на основе переданного идентификатора и позиции.
     * Принимает параметры {@code id} и {@code position} в виде целого числа.
     * Возвращает объект типа {@link Map} с единственной парой ключ/значение "position"/новой позицией преподавателя.
     *
     * @param id       Параметр типа {@link UUID}, содержащий идентификатор преподавателя.
     * @param position Параметр типа int, содержащий новую позицию преподавателя.
     * @return Объект типа {@link Map} с единственной парой ключ/значение "position"/новой позицией преподавателя.
     */
    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"pos"})
    public Map<String, Integer> updatePositionTeacher(
            @PathVariable(name = "id") UUID id,
            @RequestParam(name = "pos") int position
    ) {
        var newPosition = teacherService.updatePosition(position, id);
        return Map.of("position", newPosition);
    }

    /**
     * Удаляет преподавателя с указанным {@code id}.
     *
     * @param id идентификатор удаляемого преподавателя
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTeacher(
            @PathVariable(name = "id") UUID id
    ) {
        teacherService.removeTeacher(id);
    }

    /**
     * Возвращает файл изображения для указанного преподавателя по имени файла {@code filename}.
     *
     * @param filename имя файла изображения преподавателя
     * @return массив байтов, содержащий данные изображения в формате JPG
     */
    @GetMapping(path = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getTeacherImg(
            @PathVariable(name = "filename") String filename
    ) throws IOException {
        var bytes = teacherService.getImageTeacher(filename).getInputStream().readAllBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400,immutable")
                .body(bytes);
    }

    /**
     * Возвращает список преподавателей с возможностью пропустить некоторое количество
     * преподавателей и ограничить размер ответа.
     *
     * @param skip количество пропущенных преподавателей в начале списка
     * @param size максимальное количество возвращаемых преподавателей; -1, чтобы вернуть все результаты (по умолчанию)
     * @return список преподавателей, в количестве не превышающем размер, если он указан
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TeacherDto> getTeachers(
            @RequestParam(name = "skip", defaultValue = "0") int skip,
            @RequestParam(name = "size", defaultValue = "-1") int size
    ) {
        if (size == -1) {
            return teacherService.getTeachers(skip);
        }
        return teacherService.getTeachers(skip, size);
    }
}
