package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Review;
import ru.example.ivtserver.entities.mapper.request.ReviewRequestDto;
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
@RequestMapping(path = "/review")
@Validated
public class ReviewController {

    ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Конечная точка для создает нового объект типа {@link Review} на основе переданных параметров.
     * Возвращает созданный объект типа {@link Review} в формате JSON.
     * @param dto   Объект типа {@link ReviewRequestDto}, содержащий данные о новом объекте типа {@link Review}.
     * @param img   Изображение отзыва в формате {@link MultipartFile}.
     * @return Объект типа {@link Review} в формате JSON.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Review createReview(
            @RequestPart(name = "data") @Valid ReviewRequestDto dto,
            @RequestPart(name = "img") @Valid @FileType MultipartFile img
    ) throws IOException {
        return reviewService.addReview(dto, img);
    }

    /**
     * Конечная точка для обновления объект типа {@link Review} на основе переданных параметров.
     * Принимает данные об объекте типа {@link Review} в JSON-формате через тело запроса.
     * Возвращает обновленный объект типа {@link Review} в формате JSON.
     * @param dto   Объект типа {@link ReviewRequestDto}, содержащий обновленные данные для {@link Review}.
     * @return Обновленный объект типа {@link Review} в формате JSON.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Review updateReview(
            @RequestBody @Valid ReviewRequestDto dto
    ) {
        return reviewService.updateReview(dto);
    }

    /**
     * Конечная точка для обновления изображение объекта типа {@link Review} на основе переданных параметров.
     * Возвращает ссылку на обновленное изображение в формате Map с ключом "url".
     * @param file  Новое изображение объекта типа {@link MultipartFile}.
     * @param id    Идентификатор объекта типа UUID.
     * @return  Ссылку на обновленное изображение в формате Map с ключом "url".
     */
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public Map<String, String> updateImgReview(
            @RequestPart(name = "img") @Valid @FileType MultipartFile file,
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        var url = reviewService.updateImg(file, id);
        return Map.of("url", url);
    }

    /**
     * Конечная точка для удаления объект типа {@link Review} на основе переданного идентификатора.
     * Принимает идентификатор объекта типа UUID в параметре запроса {@code id}.
     * Возвращает ответ клиенту в формате {@link ResponseEntity} с сообщением об успешном удалении.
     * @param id Идентификатор объекта типа UUID.
     * @return Ответ клиенту в формате {@link ResponseEntity} с сообщением об успешном удалении.
     */
    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> deleteReview(
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        reviewService.removeReview(id);
        return ResponseEntity.ok("Удаление преподавателя");
    }

    /**
     * Конечная точка для получения фотографии для объекта типа {@link Review}.
     * Принимает имя файла через параметр запроса "filename".
     * Возвращает массив байтов изображения в формате JPEG.
     * @param filename Имя файла объекта типа String.
     * @return Массив байтов, представляющий изображение в формате JPEG.
     */
    @GetMapping(path = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getTeacherImg(
            @PathVariable(name = "filename") String filename
    ) throws IOException {
        return reviewService.getImageReview(filename).getInputStream().readAllBytes();
    }

}
