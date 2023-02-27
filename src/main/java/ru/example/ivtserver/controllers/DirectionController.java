package ru.example.ivtserver.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.dto.DirectionRequestDto;
import ru.example.ivtserver.services.DirectionService;

import java.util.UUID;

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
    public Direction create(
            @RequestBody DirectionRequestDto dto
    ) {
        return directionService.create(dto);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public Direction update(
            @RequestBody DirectionRequestDto dto,
            @RequestParam(name = "id") UUID id
    ) {
        return directionService.update(dto, id);
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> delete(
            @RequestParam(name = "id") UUID id
    ) {
        directionService.delete(id);
        return ResponseEntity.ok("Удаление направления");
    }
}
