package ru.example.ivtserver.entities.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.*;

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

    @JsonProperty(value = "entrants")
    List<Entrant> entrants;

    @JsonProperty(value = "teachers")
    List<Teacher> teachers;

    @JsonProperty(value = "partners")
    List<Partner> partners;

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    @Value
    public static class MainDepartment {

        Department mainInfo;

        List<SiteLink> links;

    }

}
