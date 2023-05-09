package ru.example.ivtserver.utils.image;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * Проверка для типов файла.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

    FileTypes[] imgTypes;

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.imgTypes = constraintAnnotation.type().clone();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        var contentType = file.getContentType();
        for (var fileType : imgTypes) {
           if (Objects.nonNull(contentType) && contentType.equals(fileType.getType())){
               return true;
           }
        }
        return false;
    }
}
