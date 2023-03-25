package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class EntrantRequestDto {

    UUID id;

    @NotBlank
    String title;

    List<ItemDto> items;

    @JsonCreator
    public EntrantRequestDto(
            @JsonProperty(value = "id") UUID id,
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "items") List<ItemDto> items
    ) {
        this.id = id;
        this.title = title;
        this.items = items;
    }


    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Value
    public static class ItemDto {

        UUID itemId;

        @NotBlank(message = "Поле не должно быть пустым и состоять из пробелов")
        String name;

        List<ItemPointDto> points;

        @JsonCreator
        public ItemDto(@JsonProperty(value = "id", required = false) UUID itemId,
                       @JsonProperty(value = "name", required = true) String name,
                       @JsonProperty(value = "points", required = false) List<ItemPointDto> points) {
            this.itemId = itemId;
            this.name = name;
            this.points = points;
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Value
    public static class ItemPointDto {

        UUID pointId;

        @NotBlank(message = "Поле не должно быть пустым и состоять из пробелов")
        String point;

        @JsonCreator
        public ItemPointDto(@JsonProperty(value = "id", required = false) UUID pointId,
                            @JsonProperty(value = "point", required = false) String point) {
            this.pointId = pointId;
            this.point = point;
        }
    }

}
