package ru.example.ivtserver.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Value
public class TestDto {

    String name;

    @JsonCreator
    public TestDto(
            @JsonProperty(value = "name") String name
    ){
        this.name = name;
    }

}
