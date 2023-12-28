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
import ru.example.ivtserver.entities.request.ReviewRequest;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, который представляет документ "Отзыв" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@Document
@Collection("site-content")
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    UUID id;

    @Field(name = "name")
    String name;

    @Field(name = "jobTitle")
    String jobTitle;

    @Field(name = "comment")
    String comment;

    @Field(name = "imgName")
    String imgName;

    @JsonIgnore
    @Version
    private long version;

    @JsonIgnore
    @CreatedBy
    ZonedDateTime createAt;

    @JsonIgnore
    @LastModifiedBy
    ZonedDateTime updateAt;

    public static Review of(ReviewRequest request, String fileName){
        return Review.builder()
                .name(request.getName())
                .jobTitle(request.getJobTitle())
                .comment(request.getComment())
                .imgName(fileName)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return version == review.version && Objects.equals(id, review.id) && Objects.equals(name, review.name) && Objects.equals(jobTitle, review.jobTitle) && Objects.equals(comment, review.comment) && Objects.equals(imgName, review.imgName) && Objects.equals(createAt, review.createAt) && Objects.equals(updateAt, review.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, jobTitle, comment, imgName, version, createAt, updateAt);
    }
}
