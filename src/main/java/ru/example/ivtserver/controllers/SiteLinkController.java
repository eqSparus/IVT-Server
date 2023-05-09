package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.mapper.request.SiteLinkRequestDto;
import ru.example.ivtserver.services.SiteLinkService;

import java.util.UUID;

/**
 * Контролер для создания, обновления, удаления ссылок на сайты.
 */
@CrossOrigin(origins = "http://localhost:8081", methods = {
        RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT
})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/link")
public class SiteLinkController {

    SiteLinkService siteLinkService;

    @Autowired
    public SiteLinkController(SiteLinkService siteLinkService) {
        this.siteLinkService = siteLinkService;
    }

    /**
     * Конечная точка для создания объекта типа {@link SiteLink} на основе переданных данных.
     * @param dto Объект типа {@link SiteLinkRequestDto} с данными для создания {@link SiteLink}.
     * @return Объект  {@link SiteLink} представляющий созданную ссылку.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public SiteLink createLink(
            @RequestBody @Valid SiteLinkRequestDto dto
    ) {
        return siteLinkService.createLink(dto);
    }

    /**
     * Конечная точка для обновления объекта {@link SiteLink} на основе переданных данных.
     * @param dto Объект типа {@link SiteLinkRequestDto} с данными для обновления объекта типа {@link SiteLink}.
     * @return Объект типа {@link SiteLink}, обновленный на основе переданных данных.
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public SiteLink getAllLink(
            @RequestBody @Valid SiteLinkRequestDto dto
    ) {
        return siteLinkService.updateLink(dto);
    }

    /**
     * Конечная точка для удаления объект {@link SiteLink} на основе переданного {@code id}.
     * @param id Идентификатор объекта типа UUID.
     * @return Объект типа {@link ResponseEntity} с сообщением об успешном удалении.
     */
    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> getAllLink(
            @RequestParam(name = "id") UUID id
    ) {
        siteLinkService.deleteLink(id);
        return ResponseEntity.ok("Удаление ссылки");
    }
}
