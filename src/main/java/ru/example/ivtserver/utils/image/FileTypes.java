package ru.example.ivtserver.utils.image;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Расширение файлов
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum FileTypes {
    JPEG("image/jpeg"),
    PNG("image/png");

   String type;

    FileTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
