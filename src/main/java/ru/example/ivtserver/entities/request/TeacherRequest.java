package ru.example.ivtserver.entities.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

/**
 * Класс, который представляет DTO запроса для "Преподавателя" {@link ru.example.ivtserver.entities.Teacher}.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class TeacherRequest {

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    String middleName;

    String postDepartment;

    String postTeacher;

    String postAdditional;


    @JsonCreator
    public TeacherRequest(@JsonProperty(value = "firstName", required = true) String firstName,
                          @JsonProperty(value = "lastName", required = true) String lastName,
                          @JsonProperty(value = "middleName", required = true) String middleName,
                          @JsonProperty(value = "postDepartment", required = true) String postDepartment,
                          @JsonProperty(value = "postTeacher", required = true) String postTeacher,
                          @JsonProperty(value = "postAdditional", required = true)  String postAdditional) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.postDepartment = postDepartment;
        this.postTeacher = postTeacher;
        this.postAdditional = postAdditional;
    }
}
