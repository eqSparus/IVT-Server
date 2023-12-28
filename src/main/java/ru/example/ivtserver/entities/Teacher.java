package ru.example.ivtserver.entities;

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
import ru.example.ivtserver.entities.request.TeacherRequest;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, который представляет документ "Преподаватель" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@Document
@Collection("teachers-content")
public class Teacher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    UUID id;

    @Field(name = "imgName")
    String imgName;

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

    @Version
    private long version;

    @CreatedBy
    ZonedDateTime createAt;

    @LastModifiedBy
    ZonedDateTime updateAt;

    public static Teacher of(TeacherRequest request, String fileName, int position){
        return Teacher.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .postDepartment(request.getPostDepartment())
                .postTeacher(request.getPostTeacher())
                .imgName(fileName)
                .postAdditional(request.getPostAdditional())
                .position(position)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return position == teacher.position && version == teacher.version && Objects.equals(id, teacher.id) && Objects.equals(imgName, teacher.imgName) && Objects.equals(firstName, teacher.firstName) && Objects.equals(lastName, teacher.lastName) && Objects.equals(middleName, teacher.middleName) && Objects.equals(postDepartment, teacher.postDepartment) && Objects.equals(postTeacher, teacher.postTeacher) && Objects.equals(postAdditional, teacher.postAdditional) && Objects.equals(createAt, teacher.createAt) && Objects.equals(updateAt, teacher.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imgName, firstName, lastName, middleName, postDepartment, postTeacher, postAdditional, position, version, createAt, updateAt);
    }
}
