package ru.example.ivtserver.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Review;
import ru.example.ivtserver.entities.mapper.request.ReviewRequestDto;
import ru.example.ivtserver.exceptions.NoIdException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ReviewService {

    List<Review> getAllReviews();

    Review addReview(ReviewRequestDto reviewRequest, MultipartFile img) throws IOException;

    Review updateReview(ReviewRequestDto reviewRequest) throws NoIdException;

    void removeReview(UUID id) throws IOException, NoIdException;

    Resource getImageReview(String filename);

    String updateImg(MultipartFile img, UUID id) throws IOException, NoIdException;
}
