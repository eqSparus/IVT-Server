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
import ru.example.ivtserver.entities.Partner;
import ru.example.ivtserver.entities.dto.PartnerDto;
import ru.example.ivtserver.entities.request.PartnerRequest;
import ru.example.ivtserver.services.PartnerService;
import ru.example.ivtserver.utils.image.FileType;
import ru.example.ivtserver.utils.image.FileTypes;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для создания, удаления, обновления и обновления изображения партнеров кафедры и получения логотипа.
 */
@CrossOrigin(origins = "http://localhost:8081", methods = {
        RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.PUT
})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/partners")
@Validated
@RequiredArgsConstructor
public class PartnerController {

    PartnerService partnerService;

    /**
     * Конечная точка для создания нового объекта типа {@link Partner} на основе переданных данных {@link PartnerRequest}
     * и его изображения {@link MultipartFile}, и возврата созданного объект {@link PartnerDto}.
     *
     * @param dto Объект типа {@link PartnerRequest}, содержащий данные для создания нового {@link Partner}.
     * @param img Объект типа {@link MultipartFile}, содержащий логотип для {@link Partner}.
     * @return Объект созданный {@link PartnerDto}.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public PartnerDto createTeacher(
            @RequestPart(name = "data") @Valid PartnerRequest dto,
            @RequestPart(name = "img") @Valid @FileType(type = FileTypes.PNG) MultipartFile img
    ) {
        return partnerService.addPartner(dto, img);
    }

    /**
     * Конечная точка для обновляет объект типа {@link Partner} на основе переданного объекта данных {@link PartnerRequest}
     * и возврата обновленного объект {@link PartnerDto} в формате JSON.
     *
     * @param request Объект типа {@link PartnerRequest}, содержащий данные для обновления {@link Partner}.
     * @param id      идентификатор партнера.
     * @return Объект обновленный {@link PartnerDto}.
     */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PartnerDto updateTeacher(
            @RequestBody @Valid PartnerRequest request,
            @PathVariable(name = "id") UUID id
    ) {
        return partnerService.updatePartner(request, id);
    }

    /**
     * Конечная точка для обновления изображения объекта типа {@link Partner} на основе переданных
     * данных изображения {@link MultipartFile} и {@code id} объекта. Возвращает URL обновленного изображения
     * в виде объекта типа {@link Map<String, String>} в формате JSON.
     *
     * @param file Объект типа {@link MultipartFile}, содержащий изображение для обновления существующего объекта {@link Partner}.
     * @param id UUID-идентификатор объекта типа {@link Partner}, к которому необходимо привязать обновленное изображение.
     * @return Объект типа {@link Map<String, String>}, представляющий URL обновленного изображения.
     */
    @PatchMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> updateImgTeacher(
            @RequestPart(name = "img") @Valid @FileType(type = FileTypes.PNG) MultipartFile file,
            @PathVariable(name = "id") UUID id
    ) {
        var url = partnerService.updateImg(file, id);
        return Map.of("url", url);
    }

    /**
     * Конечная точка для удаления объект типа {@link Partner} на основе переданного {@code id}
     * и возвращает объект типа {@link ResponseEntity}.
     *
     * @param id UUID-идентификатор объекта типа {@link Partner}, который необходимо удалить.
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTeacher(
            @PathVariable(name = "id") UUID id
    ) {
        partnerService.removePartner(id);
    }

    /**
     * Конечная точка для получения логотипа для объекта типа {@link Partner} на основе переданного имени файла {@code filename}.
     * Возвращает массив байтов для изображения в формате PNG.
     *
     * @param filename Имя файла изображения-логотипа для объекта типа {@link Partner}.
     * @return Массив байтов, представляющий изображение-логотип в формате PNG.
     */
    @GetMapping(path = "/image/{filename}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getLogoPartner(
            @PathVariable(name = "filename") String filename
    ) throws IOException {
        var bytes = partnerService.getLogoPartner(filename).getInputStream().readAllBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400,immutable")
                .body(bytes);
    }
}
