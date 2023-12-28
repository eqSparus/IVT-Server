package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.dto.*;

import java.util.List;

/**
 * Класс, который представляет DTO с содержимым сайта.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class SiteContentDto {

    @JsonProperty(value = "department")
    MainDepartmentDto department;

    @JsonProperty(value = "about")
    List<AboutDepartmentDto> aboutDepartment;

    @JsonProperty(value = "directions")
    List<DirectionDto> direction;

    @JsonProperty(value = "entrants")
    List<EntrantDto> entrants;

    @JsonProperty(value = "partners")
    List<PartnerDto> partners;

    @JsonProperty(value = "reviews")
    List<ReviewDto> reviews;

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @AllArgsConstructor
    @Value
    public static class MainDepartmentDto {

        DepartmentDto mainInfo;

        List<SiteLinkDto> links;

    }
}
