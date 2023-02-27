package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class SiteLinkRequestDto {

    String href;

    String icon;

    @JsonCreator
    public SiteLinkRequestDto(@JsonProperty(value = "href", required = true) String href,
                              @JsonProperty(value = "icon", required = true) String icon) {
        this.href = href;
        this.icon = icon;
    }
}
