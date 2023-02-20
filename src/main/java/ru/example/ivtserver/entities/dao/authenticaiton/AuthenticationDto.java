package ru.example.ivtserver.entities.dao.authenticaiton;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class AuthenticationDto {

    @JsonProperty("Authorization")
    String authorization;

    @JsonProperty("RefreshToken")
    String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Builder.Default
    ZonedDateTime timestamp = ZonedDateTime.now();

}
