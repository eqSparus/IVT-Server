package ru.example.ivtserver.entities.dto.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class ChangeEmailRequestDto {

    @NotBlank
    @Email
    String email;

    @JsonCreator
    public ChangeEmailRequestDto(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }
}
