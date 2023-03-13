package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class DirectionRequestDto {
    @NotBlank(message = "Заголовок не должен состоять из пробелов")
    String title;

    @NotBlank(message = "Квалификация образования не должна состоять из пробелов")
    String degree;

    @NotBlank(message = "Форма обучения не должна состоять из пробелов")
    String form;

    @Min(value = 1, message = "Продолжительность обучения должна быть больше и равно 1")
    @Max(value = 10, message = "Продолжительность обучения должна быть меньше или равно 10")
    Integer duration;

    @JsonCreator
    public DirectionRequestDto(@JsonProperty(value = "title", required = true) String title,
                               @JsonProperty(value = "degree", required = true) String degree,
                               @JsonProperty(value = "form", required = true) String form,
                               @JsonProperty(value = "duration", required = true) Integer duration) {
        this.title = title;
        this.degree = degree;
        this.form = form;
        this.duration = duration;
    }
}
