package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class TeacherRequestDto {

    UUID id;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotBlank
    String middleName;

    @NotBlank
    String postDepartment;

    @NotBlank
    String postTeacher;

    @NotBlank
    String postAdditional;


    @JsonCreator
    public TeacherRequestDto(@JsonProperty(value = "id") UUID id,
                             @JsonProperty(value = "firstName", required = true) String firstName,
                             @JsonProperty(value = "lastName", required = true) String lastName,
                             @JsonProperty(value = "middleName", required = true) String middleName,
                             @JsonProperty(value = "postDepartment", required = true) String postDepartment,
                             @JsonProperty(value = "postTeacher", required = true) String postTeacher,
                             @JsonProperty(value = "postAdditional", required = true)  String postAdditional) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.postDepartment = postDepartment;
        this.postTeacher = postTeacher;
        this.postAdditional = postAdditional;
    }
}
