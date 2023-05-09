package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.Entrant;
import ru.example.ivtserver.entities.mapper.request.EntrantRequestDto;
import ru.example.ivtserver.services.EntrantService;

import java.util.UUID;

/**
 * Контролер для создания, обновления и удаления информации абитуриенту
 */
@CrossOrigin(origins = "http://localhost:8081", methods = {
        RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT
})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/entrant")
public class EntrantController {

    EntrantService entrantService;

    @Autowired
    public EntrantController(EntrantService entrantService) {
        this.entrantService = entrantService;
    }

    /**
     * Конечная точка для создает нового объекта типа {@link Entrant} на основе
     * данных из объекта {@link EntrantRequestDto} и возврата его в ответе.
     * @param dto Объект типа {@link EntrantRequestDto}, содержащий данные для создания {@link Entrant}.
     * @return Объект созданный типа {@link Entrant}
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Entrant create(
            @RequestBody @Valid EntrantRequestDto dto
    ) {
        return entrantService.create(dto);
    }

    /**
     * Конечная точка для обновления объект типа {@link Entrant} на основе
     * данных из объекта {@link EntrantRequestDto} и возврата его в ответе.
     * @param dto Объект типа {@link EntrantRequestDto}, содержащий данные для обновления {@link Entrant}.
     * @return Объект обновленный типа {@link Entrant}.
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Entrant update(
            @RequestBody @Valid EntrantRequestDto dto
    ) {
        return entrantService.update(dto);
    }

    /**
     * Конечная точка для удаляет информации абитуриента на основе его идентификатора {@code id}.
     * @param id Идентификатор информации, который должен быть удален.
     * @return Объект типа {@link ResponseEntity} с успешным статусом и сообщением об удалении.
     */
    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> delete(
            @RequestParam(name = "id") UUID id
    ) {
        entrantService.delete(id);
        return ResponseEntity.ok("Удаление информации абитуриенту");
    }
}
