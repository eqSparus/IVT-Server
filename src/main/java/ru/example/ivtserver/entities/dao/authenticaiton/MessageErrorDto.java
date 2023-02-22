package ru.example.ivtserver.entities.dao.authenticaiton;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class MessageErrorDto {

    int status;

    String message;

    String path;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDateTime timestamp = LocalDateTime.now();
}
