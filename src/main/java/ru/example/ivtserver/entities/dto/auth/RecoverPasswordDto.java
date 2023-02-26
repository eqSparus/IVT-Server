package ru.example.ivtserver.entities.dto.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class RecoverPasswordDto {

    String password;

    @JsonCreator
    public RecoverPasswordDto(@JsonProperty(value = "password", required = true) String password) {
        this.password = password;
    }
}
