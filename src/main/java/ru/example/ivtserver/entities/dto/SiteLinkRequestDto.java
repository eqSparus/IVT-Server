package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class SiteLinkRequestDto {

    @NotBlank(message = "Ссылка не должна состоять из пробелов")
    String href;

    @NotBlank(message = "Название иконки не должно состоять из пробелов")
    String icon;

    @JsonCreator
    public SiteLinkRequestDto(@JsonProperty(value = "href", required = true) String href,
                              @JsonProperty(value = "icon", required = true) String icon) {
        this.href = href;
        this.icon = icon;
    }
}
