package ru.example.ivtserver.entities.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Класс, который представляет DTO запроса для "Направление" {@link ru.example.ivtserver.entities.Direction}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class DirectionRequest {

    @NotBlank
    String title;

    @NotBlank
    String degree;

    @NotBlank
    String form;

    @Min(value = 1)
    @Max(value = 10)
    int duration;

    @JsonCreator
    public DirectionRequest(@JsonProperty(value = "title", required = true) String title,
                            @JsonProperty(value = "degree", required = true) String degree,
                            @JsonProperty(value = "form", required = true) String form,
                            @JsonProperty(value = "duration", required = true) int duration) {
        this.title = title;
        this.degree = degree;
        this.form = form;
        this.duration = duration;
    }
}
