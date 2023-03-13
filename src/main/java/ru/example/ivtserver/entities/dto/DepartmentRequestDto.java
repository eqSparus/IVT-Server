package ru.example.ivtserver.entities.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
public class DepartmentRequestDto {

    @NotBlank(message = "Заголовок не должен состоять из пробелов")
    String title;

    @NotBlank(message = "Слоган не должен состоять из пробелов")
    String slogan;

    @NotBlank(message = "Телефон не должен состоять из пробелов")
    String phone;

    @NotBlank(message = "Почта не должна состоять из пробелов")
    @Email(message = "Должен быть адресом электронной почты")
    String email;

    @NotBlank(message = "Адрес не должен состоять из пробелов")
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
