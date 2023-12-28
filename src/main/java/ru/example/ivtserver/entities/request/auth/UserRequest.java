package ru.example.ivtserver.entities.request.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Класс, который представляет DTO для запроса на авторизацию пользователя.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class UserRequest {

    @NotBlank
    @Email
    String email;

    @NotBlank
    @Size(min = 12, max = 64)
    String password;

    @JsonCreator
    public UserRequest(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "password", required = true) String password
    ) {
        this.email = email;
        this.password = password;
    }
}
