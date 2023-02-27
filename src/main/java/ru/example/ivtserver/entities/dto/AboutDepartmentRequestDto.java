package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Value
public class AboutDepartmentRequestDto {

    String title;

    String description;

    @JsonCreator
    public AboutDepartmentRequestDto(@JsonProperty(value = "title", required = true) String title,
                                     @JsonProperty(value = "description", required = true) String description) {
        this.title = title;
        this.description = description;
    }
}
