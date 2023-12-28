package ru.example.ivtserver.entities.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.URL;

/**
 * Класс, который представляет DTO запроса для "Партнера" {@link ru.example.ivtserver.entities.Partner}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class PartnerRequest {

    @URL
    @NotBlank
    String href;

    @JsonCreator
    public PartnerRequest(@JsonProperty(value = "href", required = true) String href) {
        this.href = href;
    }
}
