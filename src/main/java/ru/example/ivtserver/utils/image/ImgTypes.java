package ru.example.ivtserver.utils.image;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ImgTypes {
    JPEG("image/jpeg"),
    PNG("image/png");

   String type;

    ImgTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
