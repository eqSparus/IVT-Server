package ru.example.ivtserver.entities.mapper.auth.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Класс, который представляет DTO для запроса на изменения пароля пользователя.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class ChangePasswordRequestDto {

    @NotBlank
    @Size(min = 12, max = 64)
    String password;

    @JsonCreator
    public ChangePasswordRequestDto(@JsonProperty(value = "password", required = true) String password) {
        this.password = password;
    }
}
