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
import ru.example.ivtserver.entities.request.DirectionRequest;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, который представляет документ "Направления" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@Document
@Collection("site-content")
public class Direction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    UUID id;

    @Field(name = "title")
    String title;

    @Field(name = "degree")
    String degree;

    @Field(name = "form")
    String form;

    @Field(name = "duration")
    int duration;

    @Field(name = "position")
    int position;

    @JsonIgnore
    @Version
    private long version;

    @JsonIgnore
    @CreatedBy
    ZonedDateTime createAt;

    @JsonIgnore
    @LastModifiedBy
    ZonedDateTime updateAt;

    public static Direction of(DirectionRequest request, int position) {
        return Direction.builder()
                .title(request.getTitle())
                .degree(request.getDegree())
                .form(request.getForm())
                .duration(request.getDuration())
                .position(position)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction direction = (Direction) o;
        return duration == direction.duration && position == direction.position && version == direction.version && Objects.equals(id, direction.id) && Objects.equals(title, direction.title) && Objects.equals(degree, direction.degree) && Objects.equals(form, direction.form) && Objects.equals(createAt, direction.createAt) && Objects.equals(updateAt, direction.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, degree, form, duration, position, version, createAt, updateAt);
    }
}
