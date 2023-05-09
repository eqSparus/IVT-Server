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
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Класс, который представляет документ "Преподаватель" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@Builder
@Document
@Collection("teachers-content")
public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    UUID id;

    @Field(name = "urlImg")
    String urlImg;

    @JsonIgnore
    @Field(name = "pathImg")
    Path pathImg;

    @Field(name = "firstName")
    String firstName;

    @Field(name = "lastName")
    String lastName;

    @Field(name = "middleName")
    String middleName;

    @Field(name = "postDepartment")
    String postDepartment;

    @Field(name = "postTeacher")
    String postTeacher;

    @Field(name = "postAdditional")
    String postAdditional;

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

}
