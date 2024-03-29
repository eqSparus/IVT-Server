package ru.example.ivtserver.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.Version;
import org.springframework.data.couchbase.core.index.QueryIndexed;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.repository.Collection;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, который представляет документ "Пользователь" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@Document
@Collection("user-records")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    UUID id;

    @Field(name = "email")
    @QueryIndexed
    String email;

    @Field(name = "password")
    String password;

    @Field(name = "role")
    UserRole role;

    @Version
    long version;

    @CreatedBy
    ZonedDateTime createAt;

    @LastModifiedBy
    ZonedDateTime updateAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return version == user.version && Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && role == user.role && Objects.equals(createAt, user.createAt) && Objects.equals(updateAt, user.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, role, version, createAt, updateAt);
    }
}
