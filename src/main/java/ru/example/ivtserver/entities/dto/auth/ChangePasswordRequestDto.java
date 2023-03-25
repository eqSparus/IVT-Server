package ru.example.ivtserver.entities.dto.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

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
