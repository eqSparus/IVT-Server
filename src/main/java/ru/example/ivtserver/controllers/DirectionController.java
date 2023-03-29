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
import ru.example.ivtserver.entities.dto.DirectionRequestDto;
import ru.example.ivtserver.services.DirectionService;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/direction")
public class DirectionController {

    DirectionService directionService;

    @Autowired
    public DirectionController(DirectionService directionService) {
        this.directionService = directionService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Direction create(
            @RequestBody @Valid DirectionRequestDto dto
    ) {
        return directionService.create(dto);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Direction update(
            @RequestBody @Valid DirectionRequestDto dto
    ) {
        return directionService.update(dto);
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> delete(
            @RequestParam(name = "id") UUID id
    ) {
        directionService.delete(id);
        return ResponseEntity.ok("Удаление направления");
    }

    @PatchMapping(params = {"firstId", "lastId"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Direction> swapPosition(
            @RequestParam(name = "firstId") UUID firstId,
            @RequestParam(name = "lastId") UUID lastId
    ) {
        return directionService.swapPosition(firstId, lastId);
    }
}
