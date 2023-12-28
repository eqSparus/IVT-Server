package ru.example.ivtserver.entities.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.AboutDepartment;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Value
public class AboutDepartmentDto {

    UUID id;

    String title;

    String description;


    public static AboutDepartmentDto of(AboutDepartment aboutDepartment) {
        return AboutDepartmentDto.builder()
                .id(aboutDepartment.getId())
                .title(aboutDepartment.getTitle())
                .description(aboutDepartment.getDescription())
                .build();
    }

}
