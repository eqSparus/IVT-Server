package ru.example.ivtserver.entities.dao.authenticaiton;

import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class MessageDto {

    int status;

    String message;

    String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
}
