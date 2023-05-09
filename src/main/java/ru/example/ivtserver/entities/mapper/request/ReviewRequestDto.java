package ru.example.ivtserver.entities.mapper.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Класс, который представляет DTO запроса для "Отзыв" {@link ru.example.ivtserver.entities.Review}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class ReviewRequestDto {

    UUID id;

    @NotBlank
    String name;

    String jobTitle;

    @NotBlank
    String comment;

    @JsonCreator
    public ReviewRequestDto(@JsonProperty(value = "id") UUID id,
                            @JsonProperty(value = "name", required = true) String name,
                            @JsonProperty(value = "jobTitle") String jobTitle,
                            @JsonProperty(value = "comment", required = true) String comment) {
        this.id = id;
        this.name = name;
        this.jobTitle = jobTitle;
        this.comment = comment;
    }
}
