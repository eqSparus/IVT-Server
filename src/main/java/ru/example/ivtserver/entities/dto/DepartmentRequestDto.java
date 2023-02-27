package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class DepartmentRequestDto {

    String title;

    String slogan;

    String phone;

    String email;

    String address;


    public DepartmentRequestDto(@JsonProperty(value = "title", required = true) String title,
                                @JsonProperty(value = "slogan", required = true) String slogan,
                                @JsonProperty(value = "phone", required = true) String phone,
                                @JsonProperty(value = "email", required = true) String email,
                                @JsonProperty(value = "address", required = true) String address) {
        this.title = title;
        this.slogan = slogan;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
}
