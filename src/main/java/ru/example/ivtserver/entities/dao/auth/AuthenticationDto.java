package ru.example.ivtserver.entities.dao.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class AuthenticationDto {

    @JsonProperty
    String accessToken;

    @JsonProperty
    String refreshToken;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();

}
