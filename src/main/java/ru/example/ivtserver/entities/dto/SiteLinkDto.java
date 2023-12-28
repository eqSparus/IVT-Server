package ru.example.ivtserver.entities.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.SiteLink;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Value
public class SiteLinkDto {

    UUID id;

    String href;

    String icon;

    public static SiteLinkDto of(SiteLink siteLink) {
        return SiteLinkDto.builder()
                .id(siteLink.getId())
                .href(siteLink.getHref())
                .icon(siteLink.getIcon())
                .build();
    }

}
