package ru.example.ivtserver.utils.image;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImgTypeValidator.class)
@Documented
public @interface ImgType {

    ImgTypes type() default ImgTypes.JPEG;

    String message() default "Неверное расширение изображения";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
