package ru.example.ivtserver.services;

import org.springframework.web.multipart.MultipartFile;
import ru.example.ivtserver.entities.Teacher;
import ru.example.ivtserver.entities.dto.TeacherDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface TeacherService {

    Teacher addTeacher(TeacherDto dto, MultipartFile img) throws IOException;

    Teacher updateTeacher(TeacherDto dto, UUID id);

    String updateImg(MultipartFile img, UUID id) throws IOException;

    void removeTeacher(UUID id) throws IOException;

    List<Teacher> getAllTeachers();
}
