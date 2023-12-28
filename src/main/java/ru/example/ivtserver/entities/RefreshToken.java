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

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, который представляет документ "Токен обновления" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@Document(expiryExpression = "${security.token.jwt.valid-time-refresh-second}")
@Collection("user-records")
public class RefreshToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    UUID id;

    @Field(name = "token")
    String token;

    @Field(name = "expiration")
    Long expiration;

    @Field(name = "userId")
    UUID userId;

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
        RefreshToken that = (RefreshToken) o;
        return version == that.version && Objects.equals(id, that.id) && Objects.equals(token, that.token) && Objects.equals(expiration, that.expiration) && Objects.equals(userId, that.userId) && Objects.equals(createAt, that.createAt) && Objects.equals(updateAt, that.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, expiration, userId, version, createAt, updateAt);
    }
}
