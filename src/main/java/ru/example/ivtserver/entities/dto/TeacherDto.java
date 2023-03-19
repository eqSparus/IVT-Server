package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class TeacherDto {

    String firstName;

    String lastName;

    String middleName;

    String post;

    String scientificDegree;


    @JsonCreator
    public TeacherDto(@JsonProperty(value = "firstName", required = true) String firstName,
                      @JsonProperty(value = "lastName", required = true) String lastName,
                      @JsonProperty(value = "middleName", required = true) String middleName,
                      @JsonProperty(value = "post", required = true) String post,
                      @JsonProperty(value = "scientificDegree", required = true) String scientificDegree) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.post = post;
        this.scientificDegree = scientificDegree;
    }
}
