package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class DepartmentRequestDto {

    @NotBlank
    String title;

    @NotBlank
    String slogan;

    @NotBlank
    String phone;

    @NotBlank
    @Email
    String email;

    @NotBlank
    String address;

    @NotNull
    UUID leaderId;

    public DepartmentRequestDto(@JsonProperty(value = "title", required = true) String title,
                                @JsonProperty(value = "slogan", required = true) String slogan,
                                @JsonProperty(value = "phone", required = true) String phone,
                                @JsonProperty(value = "email", required = true) String email,
                                @JsonProperty(value = "address", required = true) String address,
                                @JsonProperty(value = "leaderId", required = true) UUID leaderId) {
        this.title = title;
        this.slogan = slogan;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.leaderId = leaderId;
    }
}
