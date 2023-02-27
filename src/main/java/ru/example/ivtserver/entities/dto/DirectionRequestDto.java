package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class DirectionRequestDto {

    String title;

    String degree;

    String form;

    String duration;

    @JsonCreator
    public DirectionRequestDto(@JsonProperty(value = "title", required = true) String title,
                               @JsonProperty(value = "degree", required = true) String degree,
                               @JsonProperty(value = "form", required = true) String form,
                               @JsonProperty(value = "duration", required = true) String duration) {
        this.title = title;
        this.degree = degree;
        this.form = form;
        this.duration = duration;
    }
}
