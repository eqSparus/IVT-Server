package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.dto.DirectionDto;
import ru.example.ivtserver.entities.request.DirectionRequest;
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
@RequestMapping(path = "/directions")
@RequiredArgsConstructor
public class DirectionController {

    DirectionService directionService;

    /**
     * Конечная точка для создания нового направления на основе объекта {@link DirectionRequest}
     * и возврата созданного объект {@link DirectionDto}.
     *
     * @param request Объект {@link DirectionRequest}, содержащий информацию о новом направлении.
     * @return Объект {@link DirectionDto}, представляющий созданное направление.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public DirectionDto create(
            @RequestBody @Valid DirectionRequest request
    ) {
        return directionService.create(request);
    }

    /**
     * Конечная точка для обновления информации о направлении на основе объекта {@link DirectionRequest}
     * и возврата обновленного объекта {@link DirectionDto}.
     *
     * @param request Объект {@link DirectionRequest}, содержащий информацию для обновления направления.
     * @param id Объект {@link UUID}, идентификатор направления.
     * @return Объект {@link DirectionDto}, представляющий обновленную информацию о направлении.
     */
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public DirectionDto update(
            @RequestBody @Valid DirectionRequest request,
            @PathVariable(name = "id") UUID id
    ) {
        return directionService.update(request, id);
    }

    /**
     * Конечная точка для удаления направления на основе указанного идентификатора {@code id}
     *
     * @param id Идентификатор направления, которое необходимо удалить.
     */
    @DeleteMapping(path = {"/{id}"})
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable(name = "id") UUID id
    ) {
        directionService.delete(id);
    }

    /**
     * Конечная точка для смены мест двух направлений на основе их идентификаторов {@code firstId}
     * и {@code lastId} и возврата списка объектов {@link DirectionDto}.
     *
     * @param firstId Идентификатор первого направления.
     * @param lastId  Идентификатор второго направления.
     * @return Список объектов {@link DirectionDto}, представляющий направления после выполнения операции обмена.
     */
    @PatchMapping(params = {"firstId", "lastId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DirectionDto> swapPosition(
            @RequestParam(name = "firstId") UUID firstId,
            @RequestParam(name = "lastId") UUID lastId
    ) {
        return directionService.swapPosition(firstId, lastId);
    }
}
