package ru.example.ivtserver.entities.mapper.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

/**
 * Класс, который представляет DTO запроса для "Ссылки на сайт" {@link ru.example.ivtserver.entities.SiteLink}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class SiteLinkRequestDto {

    UUID id;

    @URL
    @NotBlank
    String href;

    @NotBlank
    String icon;

    @JsonCreator
    public SiteLinkRequestDto(@JsonProperty(value = "id") UUID id,
                              @JsonProperty(value = "href", required = true) String href,
                              @JsonProperty(value = "icon", required = true) String icon) {
        this.id = id;
        this.href = href;
        this.icon = icon;
    }
}
