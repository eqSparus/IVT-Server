package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.AboutDepartment;
import ru.example.ivtserver.entities.Department;
import ru.example.ivtserver.entities.Direction;
import ru.example.ivtserver.entities.SiteLink;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class SiteContentDto {

    @JsonProperty(value = "department")
    MainDepartment department;

    @JsonProperty(value = "about")
    List<AboutDepartment> aboutDepartment;

    @JsonProperty(value = "directions")
    List<Direction> direction;


    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    @Value
    public static class MainDepartment {

        Department mainInfo;

        List<SiteLink> siteLinks;

    }

}
