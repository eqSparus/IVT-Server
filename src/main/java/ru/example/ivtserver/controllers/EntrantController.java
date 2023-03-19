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
import ru.example.ivtserver.entities.dto.EntrantRequestDto;
import ru.example.ivtserver.services.EntrantService;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/entrant")
public class EntrantController {

    EntrantService entrantService;

    @Autowired
    public EntrantController(EntrantService entrantService) {
        this.entrantService = entrantService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Entrant create(
            @RequestBody @Valid EntrantRequestDto dto
    ) {
        return entrantService.create(dto);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public Entrant update(
            @RequestBody @Valid EntrantRequestDto dto,
            @RequestParam(name = "id") UUID id
    ) {
        return entrantService.update(dto, id);
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> delete(
            @RequestParam(name = "id") UUID id
    ) {
        entrantService.delete(id);
        return ResponseEntity.ok("Удаление информации абитуриенту");
    }
}
