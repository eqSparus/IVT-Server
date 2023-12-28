package ru.example.ivtserver.entities.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Класс, который представляет DTO запроса для "Информации абитуриенту" {@link ru.example.ivtserver.entities.Entrant}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class EntrantRequest {

    @NotBlank
    String title;

    List<ItemRequest> items;

    @JsonCreator
    public EntrantRequest(
            @JsonProperty(value = "title", required = true) String title,
            @JsonProperty(value = "items") List<ItemRequest> items
    ) {
        this.title = title;
        this.items = items;
    }


    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Value
    public static class ItemRequest {

        @NotBlank(message = "Поле не должно быть пустым и состоять из пробелов")
        String name;

        List<ItemPointRequest> points;

        @JsonCreator
        public ItemRequest(@JsonProperty(value = "name", required = true) String name,
                           @JsonProperty(value = "points") List<ItemPointRequest> points) {
            this.name = name;
            this.points = points;
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Value
    public static class ItemPointRequest {


        @NotBlank(message = "Поле не должно быть пустым и состоять из пробелов")
        String point;

        @JsonCreator
        public ItemPointRequest(@JsonProperty(value = "point") String point) {
            this.point = point;
        }
    }

}
