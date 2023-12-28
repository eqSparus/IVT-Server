package ru.example.ivtserver.entities.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.Direction;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Value
public class DirectionDto {

    UUID id;

    String title;

    String degree;

    String form;

    int duration;

    int position;

    public static DirectionDto of(Direction direction) {
        return DirectionDto.builder()
                .id(direction.getId())
                .title(direction.getTitle())
                .degree(direction.getDegree())
                .form(direction.getForm())
                .duration(direction.getDuration())
                .position(direction.getPosition())
                .build();
    }

}
