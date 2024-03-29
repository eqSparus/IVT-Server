package ru.example.ivtserver.entities.request.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Класс, который представляет DTO для запроса на изменения почты пользователя.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class ChangeEmailRequest {

    @NotBlank
    @Email
    String email;

    @JsonCreator
    public ChangeEmailRequest(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }
}
