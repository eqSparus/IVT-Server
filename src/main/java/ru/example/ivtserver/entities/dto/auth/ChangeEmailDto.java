package ru.example.ivtserver.entities.dto.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class ChangeEmailDto {

    String email;

    @JsonCreator
    public ChangeEmailDto(@JsonProperty(value = "email", required = true) String email) {
        this.email = email;
    }
}
