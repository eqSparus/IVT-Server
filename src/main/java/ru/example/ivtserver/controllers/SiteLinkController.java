package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.SiteLink;
import ru.example.ivtserver.entities.dto.SiteLinkDto;
import ru.example.ivtserver.entities.request.SiteLinkRequest;
import ru.example.ivtserver.services.SiteLinkService;

import java.util.UUID;

/**
 * Контролер для создания, обновления, удаления ссылок на сайты.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/links")
@RequiredArgsConstructor
public class SiteLinkController {

    SiteLinkService siteLinkService;

    /**
     * Конечная точка для создания объекта типа {@link SiteLink} на основе переданных данных.
     *
     * @param request Объект типа {@link SiteLinkRequest} с данными для создания {@link SiteLink}.
     * @return Объект  {@link SiteLinkDto} представляющий созданную ссылку.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public SiteLinkDto createLink(
            @RequestBody @Valid SiteLinkRequest request
    ) {
        return siteLinkService.createLink(request);
    }

    /**
     * Конечная точка для обновления объекта {@link SiteLink} на основе переданных данных.
     *
     * @param request Объект типа {@link SiteLinkRequest} с данными для обновления объекта типа {@link SiteLink}.
     * @param id идентификатор ссылки.
     * @return Объект типа {@link SiteLinkDto}, обновленный на основе переданных данных.
     */
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public SiteLinkDto getAllLink(
            @RequestBody @Valid SiteLinkRequest request,
            @PathVariable(name = "id") UUID id
    ) {
        return siteLinkService.updateLink(request, id);
    }

    /**
     * Конечная точка для удаления объект {@link SiteLink} на основе переданного {@code id}.
     *
     * @param id Идентификатор объекта типа {@link UUID}.
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void getAllLink(
            @PathVariable(name = "id") UUID id
    ) {
        siteLinkService.deleteLink(id);
    }
}
