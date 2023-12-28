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
import ru.example.ivtserver.entities.request.SiteLinkRequest;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, который представляет документ "Ссылка на сайт" для БД Couchbase.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
@Document
@Collection("site-content")
public class SiteLink implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationStrategy.UNIQUE)
    UUID id;

    @Field(name = "href")
    String href;

    @Field(name = "icon")
    String icon;

    @JsonIgnore
    @Version
    private long version;

    @JsonIgnore
    @CreatedBy
    ZonedDateTime createAt;

    @JsonIgnore
    @LastModifiedBy
    ZonedDateTime updateAt;

    public static SiteLink of(SiteLinkRequest request) {
        return SiteLink.builder()
                .href(request.getHref())
                .icon(request.getIcon())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteLink siteLink = (SiteLink) o;
        return version == siteLink.version && Objects.equals(id, siteLink.id) && Objects.equals(href, siteLink.href) && Objects.equals(icon, siteLink.icon) && Objects.equals(createAt, siteLink.createAt) && Objects.equals(updateAt, siteLink.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, href, icon, version, createAt, updateAt);
    }

}
