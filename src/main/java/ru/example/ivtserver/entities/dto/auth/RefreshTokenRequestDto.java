package ru.example.ivtserver.entities.dto.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class RefreshTokenRequestDto {

    @NotBlank(message = "Токен не должен состоять из пробелов")
    String token;

    @JsonCreator
    public RefreshTokenRequestDto(@JsonProperty(value = "token", required = true) String token) {
        this.token = token;
    }
}
