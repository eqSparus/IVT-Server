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
import ru.example.ivtserver.entities.Partner;
import ru.example.ivtserver.entities.mapper.request.PartnerRequestDto;
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
@RequestMapping(path = "/partner")
@Validated
public class PartnerController {

    PartnerService partnerService;

    @Autowired
    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    /**
     * Конечная точка для создания нового объекта типа {@link Partner} на основе переданных данных {@link PartnerRequestDto}
     * и его изображения {@link MultipartFile}, и возврата созданного объект {@link Partner}.
     * @param dto Объект типа {@link PartnerRequestDto}, содержащий данные для создания нового {@link Partner}.
     * @param img Объект типа {@link MultipartFile}, содержащий логотип для {@link Partner}.
     * @return Объект созданный {@link Partner}.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Partner createTeacher(
            @RequestPart(name = "data") @Valid PartnerRequestDto dto,
            @RequestPart(name = "img") @Valid @FileType(type = FileTypes.PNG) MultipartFile img
    ) throws IOException {
        return partnerService.addPartner(dto, img);
    }

    /**
     * Конечная точка для обновляет объект типа {@link Partner} на основе переданного объекта данных {@link PartnerRequestDto}
     * и возврата обновленного объект {@link Partner} в формате JSON.
     * @param dto Объект типа {@link PartnerRequestDto}, содержащий данные для обновления {@link Partner}.
     * @return Объект обновленный {@link Partner}.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Partner updateTeacher(
            @RequestBody @Valid PartnerRequestDto dto
    ) {
        return partnerService.updatePartner(dto);
    }

    /**
     * Конечная точка для обновления изображения объекта типа {@link Partner} на основе переданных
     * данных изображения {@link MultipartFile} и {@code id} объекта. Возвращает URL обновленного изображения
     * в виде объекта типа {@link Map<String, String>} в формате JSON.
     * @param file Объект типа {@link MultipartFile}, содержащий изображение для обновления существующего объекта {@link Partner}.
     * @param id UUID-идентификатор объекта типа {@link Partner}, к которому необходимо привязать обновленное изображение.
     * @return Объект типа {@link Map<String, String>}, представляющий URL обновленного изображения.
     */
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public Map<String, String> updateImgTeacher(
            @RequestPart(name = "img") @Valid @FileType(type = FileTypes.PNG) MultipartFile file,
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        var url = partnerService.updateImg(file, id);
        return Map.of("url", url);
    }

    /**
     * Конечная точка для удаления объект типа {@link Partner} на основе переданного {@code id}
     * и возвращает объект типа {@link ResponseEntity}.
     * @param id UUID-идентификатор объекта типа {@link Partner}, который необходимо удалить.
     * @return Объект типа {@link ResponseEntity<String>} со статусом успешного выполнения операции.
     */
    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> deleteTeacher(
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        partnerService.removePartner(id);
        return ResponseEntity.ok("Удаление партнера");
    }

    /**
     * Конечная точка для получения логотипа для объекта типа {@link Partner} на основе переданного имени файла {@code filename}.
     * Возвращает массив байтов для изображения в формате PNG.
     * @param filename Имя файла изображения-логотипа для объекта типа {@link Partner}.
     * @return Массив байтов, представляющий изображение-логотип в формате PNG.
     */
    @GetMapping(path = "/image/{filename}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getLogoPartner(
            @PathVariable(name = "filename") String filename
    ) throws IOException {
        return partnerService.getLogoPartner(filename).getInputStream().readAllBytes();
    }
}
