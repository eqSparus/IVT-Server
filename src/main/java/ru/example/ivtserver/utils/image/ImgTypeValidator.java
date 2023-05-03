package ru.example.ivtserver.utils.image;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImgTypeValidator implements ConstraintValidator<ImgType, MultipartFile> {

    ImgTypes imgType;

    @Override
    public void initialize(ImgType constraintAnnotation) {
        this.imgType = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        var contentType = file.getContentType();
        return !Objects.isNull(contentType) && contentType.equals(imgType.getType());
    }
}
