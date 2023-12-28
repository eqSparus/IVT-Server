package ru.example.ivtserver.entities.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.Entrant;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Value
public class EntrantDto {

    UUID id;

    String title;

    List<ItemDto> items;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    @Builder
    public static class ItemDto {

        String name;

        List<ItemPointDto> points;

        public static ItemDto of(Entrant.Item item) {
            return ItemDto.builder()
                    .name(item.getName())
                    .points(item.getPoints().stream()
                            .map(ItemPointDto::of).toList())
                    .build();
        }

    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    @Builder
    public static class ItemPointDto {
        String point;

        public static ItemPointDto of(Entrant.ItemPoint point) {
            return ItemPointDto.builder()
                    .point(point.getPoint())
                    .build();
        }
    }

    public static EntrantDto of(Entrant entrant) {
        return EntrantDto.builder()
                .id(entrant.getId())
                .title(entrant.getTitle())
                .items(entrant.getItems().stream()
                        .map(ItemDto::of).toList())
                .build();
    }

}
