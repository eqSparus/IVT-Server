package ru.example.ivtserver.entities.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

/**
 * Класс, который представляет DTO запроса для "Ссылки на сайт" {@link ru.example.ivtserver.entities.SiteLink}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class SiteLinkRequest {

    @URL
    @NotBlank
    String href;

    @NotBlank
    String icon;

    @JsonCreator
    public SiteLinkRequest(@JsonProperty(value = "href", required = true) String href,
                           @JsonProperty(value = "icon", required = true) String icon) {
        this.href = href;
        this.icon = icon;
    }
}
