package ru.example.ivtserver.utils.image;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Аннотация {@link FileType}, которая определяет расширение файла и содержит логику проверки
 * при помощи {@link FileTypeValidator}. Может применяться как к параметрам методов, так и к полям классов.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileTypeValidator.class)
@Documented
public @interface FileType {

    /**
     * Расширение которое должно быть у файла
     */
    FileTypes[] type() default FileTypes.JPEG;

    String message() default "Неверное расширение изображения";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
