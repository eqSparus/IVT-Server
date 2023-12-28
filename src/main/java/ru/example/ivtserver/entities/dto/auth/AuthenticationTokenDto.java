package ru.example.ivtserver.entities.dto.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class AuthenticationTokenDto {

    String accessToken;

    String refreshToken;

    Long expiration;

}
