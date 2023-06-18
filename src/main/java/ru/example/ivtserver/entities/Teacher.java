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
    @JsonView(DataView.Update.class)
    UUID id;

    @Field(name = "urlImg")
    @JsonView(DataView.Create.class)
    String urlImg;

    @JsonIgnore
    @Field(name = "pathImg")
    Path pathImg;

    @Field(name = "firstName")
    @JsonView(DataView.Update.class)
    String firstName;

    @Field(name = "lastName")
    @JsonView(DataView.Update.class)
    String lastName;

    @Field(name = "middleName")
    @JsonView(DataView.Update.class)
    String middleName;

    @Field(name = "postDepartment")
    @JsonView(DataView.Update.class)
    String postDepartment;

    @Field(name = "postTeacher")
    @JsonView(DataView.Update.class)
    String postTeacher;

    @Field(name = "postAdditional")
    @JsonView(DataView.Update.class)
    String postAdditional;

    @Field(name = "position")
    @JsonView(DataView.Create.class)
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
