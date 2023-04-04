package ru.example.ivtserver.entities.mapper.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class AuthenticationToken {

    @JsonProperty
    String accessToken;

    @JsonProperty
    String refreshToken;

}
