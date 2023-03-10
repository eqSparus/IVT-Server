package ru.example.ivtserver.entities.dto.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class UserRequestDto {

    @NotBlank(message = "Почта не должена состоять из пробелов")
    @Email(message = "Должен быть адресом электронной почты")
    String email;

    @NotBlank(message = "Пароль не должен состоять из пробелов")
    @Size(min = 12, max = 64, message = "Размер пароля должен быть в пределе от 12 до 64 символов")
    String password;

    @JsonCreator
    public UserRequestDto(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "password", required = true) String password
    ) {
        this.email = email;
        this.password = password;
    }
}
