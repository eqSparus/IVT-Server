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

    @NotBlank(message = "Пароль не должен состоять из пробелов")
    @Size(min = 12, max = 64, message = "Размер пароля должен быть в пределе от 12 до 64 символов")
    String password;

    @JsonCreator
    public ChangePasswordRequestDto(@JsonProperty(value = "password", required = true) String password) {
        this.password = password;
    }
}
