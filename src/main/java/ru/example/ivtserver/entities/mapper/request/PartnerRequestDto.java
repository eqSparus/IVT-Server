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
 * Класс, который представляет DTO запроса для "Партнера" {@link ru.example.ivtserver.entities.Partner}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class PartnerRequestDto {

    UUID id;
    @URL
    @NotBlank
    String href;

    @JsonCreator
    public PartnerRequestDto(@JsonProperty(value = "id") UUID id,
                             @JsonProperty(value = "href", required = true) String href) {
        this.id = id;
        this.href = href;
    }
}
