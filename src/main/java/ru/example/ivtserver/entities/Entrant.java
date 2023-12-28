package ru.example.ivtserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.repository.Collection;
import ru.example.ivtserver.entities.request.EntrantRequest;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, который представляет документ "Информации абитуриенту" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@Document
@Collection("site-content")
public class Entrant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    UUID id;

    @Field(name = "title")
    String title;

    @Builder.Default
    @Field(name = "items")
    List<Item> items = new ArrayList<>();

    @JsonIgnore
    @Version
    private long version;

    @JsonIgnore
    @CreatedBy
    ZonedDateTime createAt;

    @JsonIgnore
    @LastModifiedBy
    ZonedDateTime updateAt;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    @Builder
    public static class Item implements Serializable {

        @Field(name = "name")
        String name;

        @Field(name = "points")
        @Builder.Default
        List<ItemPoint> points = new ArrayList<>();

        public static Item of(EntrantRequest.ItemRequest item) {
            return Item.builder()
                    .name(item.getName())
                    .points(item.getPoints().stream()
                            .map(ItemPoint::of).toList())
                    .build();
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    @Builder
    public static class ItemPoint implements Serializable {

        @Field(name = "point")
        String point;

        public static ItemPoint of(EntrantRequest.ItemPointRequest point) {
            return ItemPoint.builder()
                    .point(point.getPoint())
                    .build();
        }
    }

    public static Entrant of(EntrantRequest request){
        return Entrant.builder()
                .title(request.getTitle())
                .items(request.getItems().stream()
                        .map(Entrant.Item::of).toList())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrant entrant = (Entrant) o;
        return version == entrant.version && Objects.equals(id, entrant.id) && Objects.equals(title, entrant.title) && Objects.equals(items, entrant.items) && Objects.equals(createAt, entrant.createAt) && Objects.equals(updateAt, entrant.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, items, version, createAt, updateAt);
    }
}
