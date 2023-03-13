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
import ru.example.ivtserver.entities.dto.SiteLinkRequestDto;
import ru.example.ivtserver.services.SiteLinkService;

import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/link")
public class SiteLinkController {

    SiteLinkService siteLinkService;

    @Autowired
    public SiteLinkController(SiteLinkService siteLinkService) {
        this.siteLinkService = siteLinkService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public SiteLink createLink(
            @RequestBody @Valid SiteLinkRequestDto dto
    ) {
        return siteLinkService.createLink(dto);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public SiteLink getAllLink(
            @RequestBody @Valid SiteLinkRequestDto dto,
            @RequestParam(name = "id") UUID id
    ) {
        return siteLinkService.updateLink(dto, id);
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> getAllLink(
            @RequestParam(name = "id") UUID id
    ) {
        siteLinkService.deleteLink(id);
        return ResponseEntity.ok("Удаление ссылки");
    }


}
