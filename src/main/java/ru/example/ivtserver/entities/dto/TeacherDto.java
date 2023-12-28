package ru.example.ivtserver.entities.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.utils.ImagePathConstant;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Value
@Builder
public class TeacherDto {

    UUID id;

    String urlImg;

    String firstName;

    String lastName;

    String middleName;

    String postDepartment;

    String postTeacher;

    String postAdditional;

    int position;

    public static TeacherDto of(Teacher teacher) {
        return TeacherDto.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .middleName(teacher.getMiddleName())
                .postDepartment(teacher.getPostDepartment())
                .postTeacher(teacher.getPostTeacher())
                .postAdditional(teacher.getPostAdditional())
                .position(teacher.getPosition())
                .urlImg(ImagePathConstant.BASE_TEACHER_PATH.concat("/").concat(teacher.getImgName()))
                .build();
    }
}
