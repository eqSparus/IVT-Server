package ru.example.ivtserver.entities.mapper.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Класс, который представляет DTO запроса для "О кафедре" {@link ru.example.ivtserver.entities.AboutDepartment}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class AboutDepartmentRequestDto {

    UUID id;

    @NotBlank
    String title;

    @NotBlank
    String description;

    @JsonCreator
    public AboutDepartmentRequestDto(@JsonProperty(value = "id") UUID id,
                                     @JsonProperty(value = "title", required = true) String title,
                                     @JsonProperty(value = "description", required = true) String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
