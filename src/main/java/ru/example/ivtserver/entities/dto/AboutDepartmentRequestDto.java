package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Value
public class AboutDepartmentRequestDto {

    @NotBlank(message = "Заголовок не должен состоять из пробелов")
    String title;

    @NotBlank(message = "Описания не должен состоять из пробелов")
    String description;

    @JsonCreator
    public AboutDepartmentRequestDto(@JsonProperty(value = "title", required = true) String title,
                                     @JsonProperty(value = "description", required = true) String description) {
        this.title = title;
        this.description = description;
    }
}
