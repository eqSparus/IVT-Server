package ru.example.ivtserver.entities.dao.authenticaiton;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class UserRequestDto {

    String email;

    String password;

    @JsonCreator
    public UserRequestDto(
            @JsonProperty(value = "email") String email,
            @JsonProperty(value = "password") String password
    ) {
        this.email = email;
        this.password = password;
    }
}
