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
import ru.example.ivtserver.entities.request.PartnerRequest;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, который представляет документ "Партнеры" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@Document
@Collection("site-content")
public class Partner implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    UUID id;

    @Field(name = "href")
    String href;

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

    public static Partner of(PartnerRequest request, String fileName){
        return Partner.builder()
                .href(request.getHref())
                .imgName(fileName)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partner partner = (Partner) o;
        return version == partner.version && Objects.equals(id, partner.id) && Objects.equals(href, partner.href) && Objects.equals(imgName, partner.imgName) && Objects.equals(createAt, partner.createAt) && Objects.equals(updateAt, partner.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, href, imgName, version, createAt, updateAt);
    }
}
