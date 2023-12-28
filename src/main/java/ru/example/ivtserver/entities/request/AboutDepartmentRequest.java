package ru.example.ivtserver.entities.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Класс, который представляет DTO запроса для "О кафедре" {@link ru.example.ivtserver.entities.AboutDepartment}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class AboutDepartmentRequest {

    @NotBlank
    String title;

    @NotBlank
    String description;

    @JsonCreator
    public AboutDepartmentRequest(@JsonProperty(value = "title", required = true) String title,
                                  @JsonProperty(value = "description", required = true) String description) {
        this.title = title;
        this.description = description;
    }
}
