package ru.example.ivtserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
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
import ru.example.ivtserver.entities.mapper.DataView;

import java.io.Serializable;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Класс, который представляет документ "Партнеры" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@Builder
@Document
@Collection("site-content")
public class Partner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    @JsonView(DataView.Update.class)
    UUID id;

    @Field(name = "href")
    @JsonView(DataView.Update.class)
    String href;

    @Field(name = "urlImg")
    @JsonView(DataView.Create.class)
    String urlImg;

    @JsonIgnore
    @Version
    private long version;

    @JsonIgnore
    @CreatedBy
    ZonedDateTime createAt;

    @JsonIgnore
    @LastModifiedBy
    ZonedDateTime updateAt;

}
