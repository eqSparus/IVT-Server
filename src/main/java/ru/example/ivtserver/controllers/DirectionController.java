package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.mapper.request.DirectionRequestDto;
import ru.example.ivtserver.services.DirectionService;

import java.util.List;
import java.util.UUID;

/**
 * Контролер для создания, обновления, удаления и изменения позиции направления.
 */
@CrossOrigin(origins = "http://localhost:8081", methods = {
        RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.POST
})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/direction")
public class DirectionController {

    DirectionService directionService;

    @Autowired
    public DirectionController(DirectionService directionService) {
        this.directionService = directionService;
    }

    /**
     * Конечная точка для создания нового направления на основе объекта {@link DirectionRequestDto}
     * и возврата созданного объект {@link Direction}.
     * @param dto Объект {@link DirectionRequestDto}, содержащий информацию о новом направлении.
     * @return Объект {@link Direction}, представляющий созданное направление.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Direction create(
            @RequestBody @Valid DirectionRequestDto dto
    ) {
        return directionService.create(dto);
    }

    /**
     * Конечная точка для обновления информации о направлении на основе объекта {@link DirectionRequestDto}
     * и возврата обновленного объекта {@link Direction}.
     * @param dto Объект {@link DirectionRequestDto}, содержащий информацию для обновления направления.
     * @return Объект {@link Direction}, представляющий обновленную информацию о направлении.
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Direction update(
            @RequestBody @Valid DirectionRequestDto dto
    ) {
        return directionService.update(dto);
    }

    /**
     * Конечная точка для удаления направления на основе указанного идентификатора {@code id}
     * @param id Идентификатор направления, которое необходимо удалить.
     * @return Объект типа {@link ResponseEntity} с сообщением об успешном удалении.
     */
    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> delete(
            @RequestParam(name = "id") UUID id
    ) {
        directionService.delete(id);
        return ResponseEntity.ok("Удаление направления");
    }

    /**
     * Конечная точка для смены мест двух направлений на основе их идентификаторов {@code firstId}
     * и {@code lastId} и возврата списка объектов {@link Direction}.
     * @param firstId Идентификатор первого направления.
     * @param lastId  Идентификатор второго направления.
     * @return Список объектов {@link Direction}, представляющий направления после выполнения операции обмена.
     */
    @PatchMapping(params = {"firstId", "lastId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Direction> swapPosition(
            @RequestParam(name = "firstId") UUID firstId,
            @RequestParam(name = "lastId") UUID lastId
    ) {
        return directionService.swapPosition(firstId, lastId);
    }
}
