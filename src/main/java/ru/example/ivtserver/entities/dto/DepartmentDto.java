package ru.example.ivtserver.entities.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.Department;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class DepartmentDto {

    String title;

    String slogan;

    String phone;

    String email;

    String address;

    public static DepartmentDto of(Department department) {
        return DepartmentDto.builder()
                .title(department.getTitle())
                .slogan(department.getSlogan())
                .phone(department.getPhone())
                .email(department.getEmail())
                .address(department.getAddress())
                .build();
    }
}
