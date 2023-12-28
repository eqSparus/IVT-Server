package ru.example.ivtserver.entities.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.Review;
import ru.example.ivtserver.utils.ImagePathConstant;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Value
public class ReviewDto {


    UUID id;

    String name;

    String jobTitle;

    String comment;

    String urlImg;

    public static ReviewDto of(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .name(review.getName())
                .jobTitle(review.getJobTitle())
                .comment(review.getComment())
                .urlImg(ImagePathConstant.BASE_REVIEW_PATH.concat("/").concat(review.getImgName()))
                .build();
    }

}
