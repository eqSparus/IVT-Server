package ru.example.ivtserver.controllers;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Review;
import ru.example.ivtserver.entities.mapper.request.ReviewRequestDto;
import ru.example.ivtserver.services.ReviewService;
import ru.example.ivtserver.utils.image.ImgType;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081", methods = {
        RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT
})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping(path = "/review")
@Validated
public class ReviewController {

    ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Review createReview(
            @RequestPart(name = "data") @Valid ReviewRequestDto dto,
            @RequestPart(name = "img") @Valid @ImgType MultipartFile img
    ) throws IOException {
        return reviewService.addReview(dto, img);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Review updateReview(
            @RequestBody @Valid ReviewRequestDto dto
    ) {
        return reviewService.updateReview(dto);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, params = {"id"})
    public Map<String, String> updateImgReview(
            @RequestPart(name = "img") @Valid @ImgType MultipartFile file,
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        var url = reviewService.updateImg(file, id);
        return Map.of("url", url);
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity<String> deleteReview(
            @RequestParam(name = "id") UUID id
    ) throws IOException {
        reviewService.removeReview(id);
        return ResponseEntity.ok("Удаление преподавателя");
    }

    @GetMapping(path = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getTeacherImg(
            @PathVariable(name = "filename") String filename
    ) throws IOException {
        return reviewService.getImageReview(filename).getInputStream().readAllBytes();
    }

}
