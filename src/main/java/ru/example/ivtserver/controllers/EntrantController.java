package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.Entrant;
import ru.example.ivtserver.entities.dto.EntrantDto;
import ru.example.ivtserver.entities.request.EntrantRequest;
import ru.example.ivtserver.services.EntrantService;

import java.util.UUID;

/**
 * Контролер для создания, обновления и удаления информации абитуриенту
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/entrants")
@RequiredArgsConstructor
public class EntrantController {

    EntrantService entrantService;

    /**
     * Конечная точка для создает нового объекта типа {@link EntrantDto} на основе
     * данных из объекта {@link EntrantRequest} и возврата его в ответе.
     *
     * @param request Объект типа {@link EntrantRequest}, содержащий данные для создания {@link Entrant}.
     * @return Объект созданный типа {@link EntrantDto}
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public EntrantDto create(
            @RequestBody @Valid EntrantRequest request
    ) {
        return entrantService.create(request);
    }

    /**
     * Конечная точка для обновления объект типа {@link EntrantDto} на основе
     * данных из объекта {@link EntrantRequest} и возврата его в ответе.
     *
     * @param request Объект типа {@link EntrantRequest}, содержащий данные для обновления {@link Entrant}.
     * @param id Объект типа {@link UUID}, содержащий данные для обновления {@link Entrant}.
     * @return Объект обновленный типа {@link EntrantDto}.
     */
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public EntrantDto update(
            @RequestBody @Valid EntrantRequest request,
            @PathVariable(name = "id") UUID id
    ) {
        return entrantService.update(request, id);
    }

    /**
     * Конечная точка для удаляет информации абитуриента на основе его идентификатора {@code id}.
     *
     * @param id Идентификатор информации, который должен быть удален.
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable(name = "id") UUID id
    ) {
        entrantService.delete(id);
    }
}
