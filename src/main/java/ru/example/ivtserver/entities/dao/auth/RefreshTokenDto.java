package ru.example.ivtserver.entities.dao.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class RefreshTokenDto {

    String token;

    @JsonCreator
    public RefreshTokenDto(@JsonProperty(value = "token", required = true) String token) {
        this.token = token;
    }
}
