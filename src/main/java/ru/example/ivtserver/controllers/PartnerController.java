package ru.example.ivtserver.controllers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Partner;
import ru.example.ivtserver.entities.mapper.request.PartnerRequestDto;
import ru.example.ivtserver.services.PartnerService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RestController
@RequestMapping(path = "/partner")
public class PartnerController {

    PartnerService partnerService;

    @Autowired
    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Partner createTeacher(
            @RequestPart(name = "data") PartnerRequestDto dto,
            @RequestPart(name = "img") MultipartFile img
    ) throws IOException {
        return partnerService.addPartner(dto, img);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Partner updateTeacher(
            @RequestBody PartnerRequestDto dto
    ) {
        return partnerService.updatePartner(dto);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public Map<String, String> updateImgTeacher(
            @RequestPart(name = "img") MultipartFile file,
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        var url = partnerService.updateImg(file, id);
        return Map.of("url", url);
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> deleteTeacher(
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        partnerService.removePartner(id);
        return ResponseEntity.ok("Удаление партнера");
    }
}
