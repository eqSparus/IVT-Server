package ru.example.ivtserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
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

    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    @Builder
    public static class ItemPoint implements Serializable {

        @Field(name = "point")
        String point;
    }
}
