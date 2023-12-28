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
import ru.example.ivtserver.entities.Review;
import ru.example.ivtserver.entities.dto.ReviewDto;
import ru.example.ivtserver.entities.request.ReviewRequest;
import ru.example.ivtserver.services.ReviewService;
import ru.example.ivtserver.utils.image.FileType;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Контролер для создания, обновления, удаления, обновления изображения и получения фотографии.
 */
@CrossOrigin(origins = "http://localhost:8081", methods = {
        RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT
})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/reviews")
@Validated
@RequiredArgsConstructor
public class ReviewController {

    ReviewService reviewService;

    /**
     * Конечная точка для создает нового объект типа {@link Review} на основе переданных параметров.
     * Возвращает созданный объект типа {@link ReviewDto} в формате JSON.
     *
     * @param dto Объект типа {@link ReviewRequest}, содержащий данные о новом объекте типа {@link Review}.
     * @param img Изображение отзыва в формате {@link MultipartFile}.
     * @return Объект типа {@link ReviewDto} в формате JSON.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public ReviewDto createReview(
            @RequestPart(name = "data") @Valid ReviewRequest dto,
            @RequestPart(name = "img") @Valid @FileType MultipartFile img
    ) {
        return reviewService.addReview(dto, img);
    }

    /**
     * Конечная точка для обновления объект типа {@link Review} на основе переданных параметров.
     * Принимает данные об объекте типа {@link Review} в JSON-формате через тело запроса.
     * Возвращает обновленный объект типа {@link ReviewDto} в формате JSON.
     *
     * @param dto Объект типа {@link ReviewRequest}, содержащий обновленные данные для {@link Review}.
     * @param id идентификатор отзыва.
     * @return Обновленный объект типа {@link ReviewDto} в формате JSON.
     */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ReviewDto updateReview(
            @RequestBody @Valid ReviewRequest dto,
            @PathVariable(name = "id") UUID id
    ) {
        return reviewService.updateReview(dto, id);
    }

    /**
     * Конечная точка для обновления изображение объекта типа {@link Review} на основе переданных параметров.
     * Возвращает ссылку на обновленное изображение в формате {@link Map} с ключом "url".
     *
     * @param file Новое изображение объекта типа {@link MultipartFile}.
     * @param id   Идентификатор объекта типа {@link UUID}.
     * @return Ссылку на обновленное изображение в формате Map с ключом "url".
     */
    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> updateImgReview(
            @RequestPart(name = "img") @Valid @FileType MultipartFile file,
            @PathVariable(name = "id") UUID id
    ) {
        var url = reviewService.updateImg(file, id);
        return Map.of("url", url);
    }

    /**
     * Конечная точка для удаления объект типа {@link Review} на основе переданного идентификатора.
     * Принимает идентификатор объекта типа {@link UUID} в параметре запроса {@code id}.
     * Возвращает ответ клиенту в формате {@link ResponseEntity} с сообщением об успешном удалении.
     *
     * @param id Идентификатор объекта типа {@link UUID}.
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteReview(
            @PathVariable(name = "id") UUID id
    ) {
        reviewService.removeReview(id);
    }

    /**
     * Конечная точка для получения фотографии для объекта типа {@link Review}.
     * Принимает имя файла через параметр запроса {@code filename}.
     * Возвращает массив байтов изображения в формате JPEG.
     *
     * @param filename Имя файла.
     * @return Массив байтов, представляющий изображение в формате JPEG.
     */
    @GetMapping(path = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getTeacherImg(
            @PathVariable(name = "filename") String filename
    ) throws IOException {
        var bytes = reviewService.getImageReview(filename).getInputStream().readAllBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400,immutable")
                .body(bytes);
    }

}
